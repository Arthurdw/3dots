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

  if (data.length === 0) {
    const data = [
      107423, 102772, 104625, 108720, 111021, 105459, 108591, 119075, 117689,
      110650, 115683, 124708, 118317,
    ];
    await prisma.portfolioWorthHistory.createMany({
      data: data.map((worth, index) => {
        return {
          userId: user.id,
          worth,
          createdAt: new Date(
            Date.now() - (data.length - index) * 1000 * 60 * 60 * 24
          ),
        };
      }),
    });

    return c.json(data);
  }

  const mapped = data.map((d) => d.worth);

  return c.json(mapped);
};
