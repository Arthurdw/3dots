import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

const CACHE_TTL = 60 * 30; // 30 minutes

export default async (c: Context) => {
  const symbol = c.req.param("id");

  const prisma = db(c);
  const user = getAuthUser(c);

  await prisma.followedStocks.delete({
    where: {
      userId_stock: {
        userId: user.id,
        stock: symbol,
      },
    },
  });

  return c.json({ message: "success" });
};
