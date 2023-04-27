// "/stock/:id" GET route
import { Context } from "hono";
import { cachedFetch } from "../utils/cache";

const CACHE_TTL = 60; // One minute

interface IntradayData {
  "Time Series (5min)": {
    [key: string]: {
      "1. open": string;
      "2. high": string;
      "3. low": string;
      "4. close": string;
      "5. volume": string;
    };
  };
}

class Url {
  public static readonly OVERVIEW = Url.make(
    "https://www.alphavantage.co/query?function=OVERVIEW"
  );
  public static readonly INTRADAY = Url.make(
    "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&interval=5min"
  );
  public static readonly QUOTE = Url.make(
    "https://www.alphavantage.co/query?function=GLOBAL_QUOTE"
  );

  public static readonly type = Url.make("");

  private static make(baseUrl: string) {
    return (symbol: string, apiKey: string) =>
      `${baseUrl}&symbol=${symbol}&apikey=${apiKey}`;
  }
}

type UrlType = typeof Url.type; // All endpoints have the same type

export default async (c: Context) => {
  const symbol = c.req.param("id");
  const fetch = (endpoint: UrlType) =>
    cachedFetch(endpoint(symbol, c.env.STOCKS_API_TOKEN), CACHE_TTL);

  const responses = await Promise.all([
    fetch(Url.OVERVIEW),
    fetch(Url.INTRADAY),
  ]);

  const [overviewData, intradayData] = <[Object, IntradayData]>(
    await Promise.all(responses.map((r) => r.json()))
  );
  const intradayDataExtracted = Object.values(
    intradayData["Time Series (5min)"]
  );
  const currentIntraday = intradayDataExtracted[0];
  const intraday = intradayDataExtracted.map((d) => parseFloat(d["2. high"]));

  return c.json({
    ...overviewData,
    intraday,
    open: parseFloat(currentIntraday["1. open"]),
    bid: parseFloat(currentIntraday["2. high"]),
    ask: parseFloat(currentIntraday["3. low"]),
    close: parseFloat(currentIntraday["4. close"]),
  });
};
