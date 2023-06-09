import { db } from "../utils/db";
import { Context } from "hono";
import { getAuthUser } from "../utils/jwt";

export default async (c: Context) => {
  const prisma = db(c);
  const id = getAuthUser(c).id;
  const user = await prisma.user.findUnique({ where: { id } });

  if (!user) {
    return new Response(
      JSON.stringify({
        success: false,
        message: "User not found",
      }),
      { status: 404 }
    );
  }

  return c.json(user);
};
