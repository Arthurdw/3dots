import { Context } from "hono";
import { cachedFetch, fetchQuote } from "../utils/cache";
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
      let quote = {
        price: 0,
        open: 0,
      };
      try {
        quote = await fetchQuote(c, d.symbol, 60 * 30);
      } catch (err) {
        console.debug("Could not fetch quote for symbol", d.symbol);
      }

      return {
        ...d,
        price: quote.price,
        lastPrice: quote.open,
      };
    })
  );

  return c.json(withFinancials);
};
