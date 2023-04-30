import { Context } from "hono";
import { fetchOverview, fetchQuote } from "../utils/cache";
import { db } from "../utils/db";

const CACHE_TTL = 60; // One minute

export default async (c: Context) => {
  const symbol = c.req.param("id");
  const prisma = db(c);

  let [quote, { Name: stockName }] = await Promise.all([
    fetchQuote(c, symbol, CACHE_TTL),
    prisma.stockDetailsCache.findFirst({
      where: { Symbol: symbol },
      select: {
        Name: true,
      },
    }),
  ]);

  if (!stockName) {
    try {
      let data = await fetchOverview(c, symbol, CACHE_TTL);
      stockName = data.Name;
    } catch (err) {
        return c.json(
            { message: "Could not get stock details, please try again." },
            { status: 500 }
        );
    }
  }

  return c.json({
    stockName,
    symbol,
    price: quote.price,
  });
};
