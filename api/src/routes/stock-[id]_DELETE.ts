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

  let originalSpendPerStock = stock.spent / stock.amount;
  let gained = quote.price * amount - originalSpendPerStock * amount;
  let spent = stock.spent - gained;

  await prisma.picks.update({
    where: {
      userId_stock: {
        stock: symbol,
        userId: user.id,
      },
    },
    data: { amount, spent },
  });

  const userGained = user.gained + gained;

  await prisma.user.update({
    where: {
      id: user.id,
    },
    data: {
      gained: userGained,
    },
  });

  await prisma.portfolioWorthHistory.create({
    data: {
      userId: user.id,
      worth: userGained,
    },
  });

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

  return c.json({ symbol, amount, gained });
};
