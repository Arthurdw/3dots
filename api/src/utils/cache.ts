import { StockEndpoints } from "./stock-endpoints";
import { Context } from "hono";
import { db } from "./db";

export const cachedFetch = async (url: string, time = 60 * 60) => {
  const res = await fetch(url, {
    cf: {
      cacheTtl: time,
      cacheEverything: true,
      cacheKey: url,
    },
  });

  return res
}

export const fetchOverview = async (
  c: Context,
  symbol: string,
  TTL: number
): Promise<OverviewData> => {
  const res = await cachedFetch(
    StockEndpoints.OVERVIEW(symbol, c.env.STOCKS_API_TOKEN),
    TTL
  );
  const prisma = db(c);
  const val = await res.json();

  if (val?.["Note"]) {
    const cache = await prisma.stockDetailsCache.findUnique({
      where: {
        Symbol: symbol,
      },
    });

    if (!cache) {
      throw new Error("Could not get details of stock. Please try again later.");
    }

    const data = {
      ...cache,
      "52WeekHigh": cache.FiftyTwoWeekHigh,
      "52WeekLow": cache.FiftyTwoWeekLow,
      "50DayMovingAverage": cache.FiftyDayMovingAverage,
      "200DayMovingAverage": cache.TwoHundredDayMovingAverage,
    };

    delete data.FiftyTwoWeekHigh;
    delete data.FiftyTwoWeekLow;
    delete data.FiftyDayMovingAverage;
    delete data.TwoHundredDayMovingAverage;

    return data;
  }
  const value = val as OverviewData;
  const data = {
    ...value,
    FiftyTwoWeekHigh: value["52WeekHigh"],
    FiftyTwoWeekLow: value["52WeekLow"],
    FiftyDayMovingAverage: value["50DayMovingAverage"],
    TwoHundredDayMovingAverage: value["200DayMovingAverage"],
  };

  delete data["52WeekHigh"];
  delete data["52WeekLow"];
  delete data["50DayMovingAverage"];
  delete data["200DayMovingAverage"];

  await prisma.stockDetailsCache.upsert({
    where: {
      Symbol: symbol,
    },
    update: data,
    create: data,
  });

  return value;
};

interface IntradayResponse {
  intraday: number[];
  open: number;
  bid: number;
  ask: number;
  close: number;
}

export const fetchIntraday = async (
  c: Context,
  symbol: string,
  TTL: number
): Promise<IntradayResponse> => {
  const res = await cachedFetch(
    StockEndpoints.INTRADAY(symbol, c.env.STOCKS_API_TOKEN),
    TTL
  );
  const prisma = db(c);
  const val = await res.json();

  if (val?.["Note"]) {
    const cachedData = await prisma.stockIntradayCache.findUnique({
      where: {
        Symbol: symbol,
      },
      select: {
        data: true,
        open: true,
        bid: true,
        ask: true,
        close: true,
      },
    });

    return {
      intraday: JSON.parse(cachedData.data),
      open: cachedData.open,
      bid: cachedData.bid,
      ask: cachedData.ask,
      close: cachedData.close,
    };
  }

  const intradayData = val as IntradayData;
  const intradayDataExtracted = Object.values(
    intradayData["Time Series (5min)"]
  );
  const currentIntraday = intradayDataExtracted[0];
  const parsed = {
    intraday: intradayDataExtracted.map((d) => parseFloat(d["2. high"])),
    open: parseFloat(currentIntraday["1. open"]),
    bid: parseFloat(currentIntraday["2. high"]),
    ask: parseFloat(currentIntraday["3. low"]),
    close: parseFloat(currentIntraday["4. close"]),
  };

  const generalData = {
    data: JSON.stringify(parsed.intraday),
    open: parsed.open,
    bid: parsed.bid,
    ask: parsed.ask,
    close: parsed.close,
  };
  await prisma.stockIntradayCache.upsert({
    where: {
      Symbol: symbol,
    },
    update: generalData,
    create: {
      Symbol: symbol,
      ...generalData,
    },
  });

  return parsed;
};

interface QuoteCache {
  symbol: string;
  open: number;
  price: number;
}

export const fetchQuote = async (
  c: Context,
  symbol: string,
  TTL: number
): Promise<QuoteCache> => {
  const res = await cachedFetch(
    StockEndpoints.QUOTE(symbol, c.env.STOCKS_API_TOKEN),
    TTL
  );

  const prisma = db(c);
  const val = await res.json();

  if (val?.["Note"]) {
    const cachedData = await prisma.stockQuoteCache.findUnique({
      where: { symbol },
      select: {
        open: true,
        price: true,
      },
    });

    return {
      symbol,
      open: cachedData.open,
      price: cachedData.price,
    };
  }

  const quoteData = val as QuoteData;
  const quoteGlobal = quoteData["Global Quote"];
  const parsed = {
    symbol,
    open: parseFloat(quoteGlobal["02. open"]),
    price: parseFloat(quoteGlobal["05. price"]),
  };

  await prisma.stockQuoteCache.upsert({
    where: { symbol },
    update: parsed,
    create: parsed,
  });

  return parsed;
};
