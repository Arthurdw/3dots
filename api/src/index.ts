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

app.post("/login", async (c) => {
  // TODO: Google OAuth verify
  const prisma = db(c);
  const user = await prisma.user.findUnique({
    where: { id: "clgqj11jw0000o40h70ajkwnm" },
  });
  // TODO: Add expiration
  const token = await jwt.sign({ user }, c.env.JWT_SECRET);
  return c.json({ token });
});

app.post("/users", zValidator("json", UserCreateSchema), async (c) => {
  const prisma = db(c);
  const data: z.infer<typeof UserCreateSchema> = c.req.valid("json");

  const user = await prisma.user.create({
    data: {
      username: data.username,
      email: data.email,
    },
  });

  return c.json(user);
});

app.get("/users/me", requiresAuth, async (c) => {
  const prisma = db(c);
  console.log(c.get("jwtPayload").user);
  const user = await prisma.user.findUnique({
    where: { id: c.get("jwtPayload").user.id },
  });
  return c.json(user);
});

export default app;
