import { Context } from "hono";
import { fetchQuote } from "../utils/cache";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

const CACHE_TTL = 60; // 1 minute

export default async (c: Context) => {
  const symbol = c.req.param("id");
  let amount = parseFloat(c.req.query("amount")) || 1;

  const quote = await fetchQuote(c, symbol, CACHE_TTL);

  const prisma = db(c);
  const user = getAuthUser(c);

  const stock = await prisma.picks.findUnique({
    where: {
      userId_stock: {
        stock: symbol,
        userId: user.id,
      },
    },
  });

  if (!stock) {
    return new Response(
      JSON.stringify({
        success: false,
        message: "Stock not in portfolio",
      }),
      { status: 400 }
    );
  } else if (stock.amount < amount) {
    return new Response(
      JSON.stringify({
        success: false,
        message: "Not enough stocks to sell",
      }),
      { status: 400 }
    );
  }

  const gained = quote.price * amount;
  const amountLeft = stock.amount - amount;

  if (amountLeft === 0) {
    await prisma.picks.delete({
      where: {
        userId_stock: {
          stock: symbol,
          userId: user.id,
        },
      },
    });
  } else {
    await prisma.picks.update({
      where: {
        userId_stock: {
          stock: symbol,
          userId: user.id,
        },
      },
      data: { amount: amountLeft },
    });
  }

  const fetchedUser = await prisma.user.findUnique({
    where: {
        id: user.id,
    },
    select: {
        gained: true,
    }
  });

  await prisma.user.update({
    where: {
      id: user.id,
    },
    data: {
      gained: fetchedUser.gained + gained,
    },
  });

  const history = await prisma.portfolioWorthHistory.findMany({
    where: {
        userId: user.id,
    },
    select: {
        worth: true,
    }
  });
  const historyWorth = history?.[0]?.worth || 0;

  await prisma.portfolioWorthHistory.create({
    data: {
      userId: user.id,
      worth: historyWorth + gained,
    },
  });

  if (history.length > 30) {
    await prisma.portfolioWorthHistory.delete({
      where: {
        userId_createdAt: {
          userId: user.id,
          createdAt: (
              await prisma.portfolioWorthHistory.findFirst({
                where: {
                  userId: user.id,
                },
                orderBy: {
                  createdAt: "asc",
                },
              })
          ).createdAt,
        },
      },
    });
  }

  return c.json({ symbol, amount, gained });
};
