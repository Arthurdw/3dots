import { PrismaClient } from "@prisma/client/edge";
import { Context, Hono, Next } from "hono";
import { poweredBy } from "hono/powered-by";
import { prettyJSON } from "hono/pretty-json";
import { logger } from "hono/logger";
import { etag } from "hono/etag";
import { jwt as jwtMiddleware } from "hono/jwt";
import { Jwt as jwt } from "hono/utils/jwt";
import { z } from "zod";
import { zValidator } from "@hono/zod-validator";

const hono = new Hono();

const UserCreateSchema = z.object({
  username: z.string(),
  email: z.string().email(),
});

const UserLoginSchema = z.object({ token: z.string() });

interface GoogleTokenResponse {
  iss: string;
  azp: string;
  aud: string;
  sub: string;
  email: string;
  email_verified: string;
  name: string;
  picture: string;
  given_name: string;
  family_name: string;
  locale: string;
  iat: string;
  exp: string;
  alg: string;
  kid: string;
  typ: string;
}

const getGoogleTokenData = async (
  token: string
): Promise<GoogleTokenResponse> => {
  const url = `https://oauth2.googleapis.com/tokeninfo?id_token=${token}`;
  const res = await fetch(url);
  if (res.ok) return (await res.json()) as GoogleTokenResponse;
  return null;
};

const requiresAuth = (c: Context, next: Next) => {
  // @ts-ignore
  return jwtMiddleware({ secret: c.env.JWT_SECRET })(c, next);
};

const db = (c: Context) =>
  new PrismaClient({
    datasources: {
      db: {
        url: c.env.DATABASE_URL,
      },
    },
  });

hono.use("*", poweredBy(), prettyJSON(), logger(), etag());
hono.get("/", (c) =>
  c.text(
    "Welcome to the 3dots API! All endpoints are scoped under the /v1 path."
  )
);

const app = hono.route("/v1");

app.post("/users", zValidator("json", UserLoginSchema), async (c) => {
  const data: z.infer<typeof UserLoginSchema> = c.req.valid("json");
  const googleToken = await getGoogleTokenData(data.token);

  if (
    !googleToken ||
    googleToken.azp !== c.env.GOOGLE_APP_ID ||
    googleToken.aud !== c.env.GOOGLE_CLIENT_ID
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
        username: googleToken.name,
        email: googleToken.email,
      },
    });
  }

  const token = await jwt.sign({ user }, <string>c.env.JWT_SECRET);
  return c.json({ token });
});

app.get("/users/me", requiresAuth, async (c) => {
  const prisma = db(c);
  // @ts-ignore
  console.log(c.get("jwtPayload").user);
  const user = await prisma.user.findUnique({
    // @ts-ignore
    where: { id: c.get("jwtPayload").user.id },
  });
  return c.json(user);
});

export default hono;
