import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import { StockEndpoints, StockEndpointType } from "../utils/stock-endpoints";

const CACHE_TTL = 60; // One minute

export default async (c: Context) => {
  const symbol = c.req.param("id");
  const fetch = (endpoint: StockEndpointType) =>
    cachedFetch(endpoint(symbol, c.env.STOCKS_API_TOKEN), CACHE_TTL);

  const responses = await Promise.all([
    fetch(StockEndpoints.OVERVIEW),
    fetch(StockEndpoints.INTRADAY),
  ]);

  const [overviewData, intradayData] = <[Object, IntradayData]>(
    await Promise.all(responses.map((r) => r.json()))
  );
  const intradayDataExtracted = Object.values(
    intradayData["Time Series (5min)"]
  );
  const currentIntraday = intradayDataExtracted[0];
  const intraday = intradayDataExtracted.map((d) => parseFloat(d["2. high"]));

  console.log(JSON.stringify(overviewData))

  return c.json({
    ...overviewData,
    intraday,
    open: parseFloat(currentIntraday["1. open"]),
    bid: parseFloat(currentIntraday["2. high"]),
    ask: parseFloat(currentIntraday["3. low"]),
    close: parseFloat(currentIntraday["4. close"]),
  });
};
