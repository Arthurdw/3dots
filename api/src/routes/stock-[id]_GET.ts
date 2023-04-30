import { Context } from "hono";
import { fetchIntraday, fetchOverview } from "../utils/cache";

const CACHE_TTL = 60; // One minute

export default async (c: Context) => {
  const symbol = c.req.param("id");

  try {
    const [overviewData, intradayData] = await Promise.all([
      fetchOverview(c, symbol, CACHE_TTL),
      fetchIntraday(c, symbol, CACHE_TTL),
    ]);

    return c.json({
      ...overviewData,
      ...intradayData,
    });
  } catch (err) {
    console.error(err);
    return c.json(
      { message: "Could not get stock details, please try again." },
      { status: 500 }
    );
  }
};
