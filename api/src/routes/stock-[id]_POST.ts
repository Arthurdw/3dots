import { Context } from "hono";
import { fetchOverview, fetchQuote } from "../utils/cache";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

const CACHE_TTL = 60 * 30; // 30 minutes

export default async (c: Context) => {
  const symbol = c.req.param("id");
  let amount = parseFloat(c.req.query("amount")) || 1;

  try {
    const [quote, overview] = await Promise.all([
      fetchQuote(c, symbol, CACHE_TTL),
      fetchOverview(c, symbol, CACHE_TTL),
    ]);

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

    let spent = quote.price * amount;

    if (!stock) {
      await prisma.picks.create({
        data: {
          stock: symbol,
          userId: user.id,
          amount,
          spent,
          stockName: overview.Name,
        },
      });
    } else {
      amount = stock.amount + amount;
      spent = stock.spent + spent;
      await prisma.picks.update({
        where: {
          userId_stock: {
            stock: symbol,
            userId: user.id,
          },
        },
        data: { amount, spent },
      });
    }

    await prisma.user.update({
      where: {
        id: user.id,
      },
      data: {
        spent: user.spent + spent,
      },
    });

    return c.json({ symbol, amount, spent });
  } catch (err) {
    return c.json(
      { message: "Could not get stock details, please try again." },
      { status: 500 }
    );
  }
};
