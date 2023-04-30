import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";
import { fetchQuote } from "../utils/cache";

const CACHE_TTL = 60 * 30; // 30 minutes

export default async (c: Context) => {
  const prisma = db(c);
  const user = getAuthUser(c);

  const data = await prisma.followedStocks.findMany({
    where: {
      userId: user.id,
    },
    select: {
      stock: true,
      stockName: true,
    },
  });

  const withFinancials = await Promise.all([
    ...data.map(async (stock) => {
      const quote = await fetchQuote(c, stock.stock, CACHE_TTL);
      return {
        ...stock,
        price: quote.price,
        lastPrice: quote.open,
      };
    }),
  ]);

  return c.json(withFinancials);
};
