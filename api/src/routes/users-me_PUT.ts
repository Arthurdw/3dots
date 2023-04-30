import { Context } from "hono";
import { db } from "../utils/db";
import { getAuthUser } from "../utils/jwt";
import { z } from "zod";
import { UserChangeDetails } from "../schema/user-change-details";

export default async (c: Context) => {
  const data: z.infer<typeof UserChangeDetails> = c.req.valid("json");
  const prisma = db(c);
  const user = getAuthUser(c);

  const updatedUser = await prisma.user.update({
    where: {
      id: user.id,
    },
    data: {
      username: data.username,
    },
    select: {
      username: true,
      email: true,
      id: true,
      spent: true,
      gained: true,
      updatedAt: true,
      createdAt: true,
    },
  });

  return c.json(updatedUser);
};
