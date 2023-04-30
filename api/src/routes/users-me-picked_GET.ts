import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";
import {cachedFetch, fetchQuote} from "../utils/cache";
import { StockEndpoints } from "../utils/stock-endpoints";

export default async (c: Context) => {
  const prisma = db(c);
  const user = getAuthUser(c);

  const stocks = await prisma.picks.findMany({
    where: {
      userId: user.id,
    },
    select: {
      stock: true,
      amount: true,
      spent: true,
      stockName: true,
    },
  });

  const results = await Promise.all(
    Object.values(stocks).map(async (stock) => {
      const quote = await fetchQuote(c, stock.stock, 60 * 30);
      return {
        ...stock,
        currentPrice: quote.price,
      };
    })
  );

  return c.json(results);
};
