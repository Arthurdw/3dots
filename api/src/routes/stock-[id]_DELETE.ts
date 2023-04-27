import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import { StockEndpoints } from "../utils/stock-endpoints";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

export default async (c: Context) => {
  const symbol = c.req.param("id");
  let amount = parseFloat(c.req.query("amount")) || 1;

  const quote = await cachedFetch(
    StockEndpoints.QUOTE(symbol, c.env.STOCKS_API_TOKEN)
  );
  const data: QuoteData = await quote.json();
  const sellPrice = parseFloat(data["Global Quote"]["05. price"]);

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

  if (!stock) {
    return new Response(
      JSON.stringify({
        success: false,
        message: "Stock not in portfolio",
      }),
      { status: 400 }
    );
  }

  let originalSpendPerStock = stock.spent / stock.amount;
  let gained = sellPrice * amount - originalSpendPerStock * amount;
  let spent = stock.spent - gained;

  await prisma.picks.update({
    where: {
      userId_stock: {
        stock: symbol,
        userId: user.id,
      },
    },
    data: { amount, spent },
  });

  await prisma.user.update({
    where: {
      id: user.id,
    },
    data: {
      gained: user.gained + gained,
    },
  });

  return c.json({ symbol, amount, gained });
};
