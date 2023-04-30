import { Context } from "hono";
import { fetchOverview } from "../utils/cache";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

const CACHE_TTL = 60 * 30; // 30 minutes

export default async (c: Context) => {
  const symbol = c.req.param("id");

  try {
    const stockData = await fetchOverview(c, symbol, CACHE_TTL);

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
  } catch (err) {
    return c.json(
      { message: "Could not get stock details, please try again." },
      { status: 500 }
    );
  }
};
