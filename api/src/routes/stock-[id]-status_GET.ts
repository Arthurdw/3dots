import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

export default async (c: Context) => {
  const symbol = c.req.param("id");

  const prisma = db(c);
  const user = getAuthUser(c);

  const where = {
    userId: user.id,
    stock: symbol,
  };

  let [picked, followed] = await Promise.all([
    prisma.picks.findFirst({
      where,
      select: {
        stock: true,
        stockName: true,
        spent: true,
        amount: true,
      },
    }),
    prisma.followedStocks.findFirst({
      where,
      select: {
        stock: true,
        stockName: true,
      },
    }),
  ]);

  return c.json({ picked, followed });
};
