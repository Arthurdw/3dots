import { z } from "zod";
import { Jwt as jwt } from "hono/utils/jwt";
import { db } from "../utils/db";
import { UserLogin } from "../schema/user-login";
import { getGoogleTokenData } from "../utils/google";
import { ZCtx } from "../types/z";

export default async (c: ZCtx<typeof UserLogin>) => {
  const data: z.infer<typeof UserLogin> = c.req.valid("json");
  const googleToken = await getGoogleTokenData(data.token);

  if (
    !googleToken ||
    googleToken.azp !== c.env.GOOGLE_APP_ID ||
    googleToken.aud !== c.env.GOOGLE_WEB_ID
  ) {
    return new Response(
      JSON.stringify({
        message: "Invalid Google token",
      }),
      { status: 401 }
    );
  }

  if (!googleToken.email_verified) {
    return new Response(
      JSON.stringify({
        message: "Email not verified",
      }),
      { status: 401 }
    );
  }

  const prisma = db(c);
  let user = await prisma.user.findUnique({
    where: { email: googleToken.email },
  });

  if (!user) {
    user = await prisma.user.create({
      data: {
        username: googleToken.given_name,
        email: googleToken.email,
      },
    });
  }

  const token = await jwt.sign({ user }, <string>c.env.JWT_SECRET);
  return c.json({ token });
};
