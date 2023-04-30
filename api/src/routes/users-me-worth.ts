import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";

export default async (c: Context) => {
  const prisma = db(c);
  const user = getAuthUser(c);

  const data = await prisma.portfolioWorthHistory.findMany({
    where: {
      userId: user.id,
    },
    select: {
      worth: true,
    },
    orderBy: {
      createdAt: "asc",
    },
  });

  const mapped = data.map((d) => d.worth);

  return c.json(mapped);
};
