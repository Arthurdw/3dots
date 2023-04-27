import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import { StockEndpoints, StockEndpointType } from "../utils/stock-endpoints";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

const CACHE_TTL = 60 * 30; // 30 minutes

export default async (c: Context) => {
  const symbol = c.req.param("id");
  let amount = parseFloat(c.req.query("amount")) || 1;

  const fetch = (endpoint: StockEndpointType) =>
    cachedFetch(endpoint(symbol, c.env.STOCKS_API_TOKEN), CACHE_TTL);

  const [quote, overview] = await Promise.all([
    fetch(StockEndpoints.QUOTE),
    fetch(StockEndpoints.OVERVIEW),
  ]);

  const quoteData: QuoteData = await quote.json();
  const overviewData: OverviewData = await overview.json();
  const sellPrice = parseFloat(quoteData["Global Quote"]["05. price"]);

  const prisma = db(c);
  const user = getAuthUser(c);

  const stock = await prisma.picks.findUnique({
    where: {
      userId_stock: {
        stock: symbol,
        userId: user.id,
      },
    },
  });

  let spent = sellPrice * amount;

  if (!stock) {
    await prisma.picks.create({
      data: {
        stock: symbol,
        userId: user.id,
        amount,
        spent,
        stockName: overviewData.Name,
      },
    });
  } else {
    amount = stock.amount + amount;
    spent = stock.spent + spent;
    await prisma.picks.update({
      where: {
        userId_stock: {
          stock: symbol,
          userId: user.id,
        },
      },
      data: { amount, spent },
    });
  }

  await prisma.user.update({
    where: {
      id: user.id,
    },
    data: {
      spent: user.spent + spent,
    },
  });

  return c.json({ symbol, amount, spent });
};
