import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import { StockEndpoints } from "../utils/stock-endpoints";

export default async (c: Context) => {
  const limit = parseInt(c.req.query("limit")) || 20;
  const offset = parseInt(c.req.query("offset")) || 0;
  const search = c.req.query("search") || "a";

  const data = await cachedFetch(
    `${StockEndpoints.BASE_URL}?function=SYMBOL_SEARCH&keywords=${search}&apikey=${c.env.STOCKS_API_TOKEN}&limit=${limit}&offset=${offset}`,
    60 * 60 * 24 * 7
  );

  const json: SymbolSearchData = await data.json();
  const remapped = json.bestMatches.map((d) => ({
    symbol: d["1. symbol"],
    name: d["2. name"],
  }));

  const withFinancials = await Promise.all(
    remapped.map(async (d) => {
      // TODO: Find a way to not have to deal with the api blocking
      // const financials = await cachedFetch(
      //   StockEndpoints.QUOTE(d.symbol, c.env.STOCKS_API_TOKEN),
      //   60 * 30
      // );
      //
      //   const financialsJson: QuoteData = await financials.json();

        return {
            ...d,
          lastPrice: 0,
            price: 0,
            // lastPrice: parseFloat(financialsJson["Global Quote"]["02. open"]),
            // price: parseFloat(financialsJson["Global Quote"]["05. price"]),
        }
    })
  );

  return c.json(withFinancials)
};
