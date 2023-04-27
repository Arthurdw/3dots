import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";
import { cachedFetch } from "../utils/cache";
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
      const quote = await cachedFetch(
        StockEndpoints.QUOTE(stock.stock, c.env.STOCKS_API_TOKEN),
        60 * 30
      );
      const data: QuoteData = await quote.json();
      const sellPrice = parseFloat(data["Global Quote"]["02. open"]);
      return {
        ...stock,
        currentPrice: sellPrice,
      };
    })
  );

  return c.json(results);
};
