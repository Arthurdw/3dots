import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import { StockEndpoints } from "../utils/stock-endpoints";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

const CACHE_TTL = 60 * 30; // 30 minutes

export default async (c: Context) => {
  const symbol = c.req.param("id");

  const data = await cachedFetch(
    StockEndpoints.OVERVIEW(symbol, c.env.STOCKS_API_TOKEN),
    CACHE_TTL
  );

  const stockData: OverviewData = await data.json();

  const prisma = db(c);
  const user = getAuthUser(c);

  await prisma.followedStocks.create({
    data: {
      stock: symbol,
      userId: user.id,
      stockName: stockData.Name,
    },
  });

  return c.json({ message: "success" });
};
