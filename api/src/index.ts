import { PrismaClient } from "@prisma/client/edge";
import { Context, Hono } from "hono";
import { poweredBy } from "hono/powered-by";
import { prettyJSON } from "hono/pretty-json";
import { logger } from "hono/logger";
import { etag } from "hono/etag";
import { z } from "zod";
import { zValidator } from "@hono/zod-validator";
import { Environment } from "hono/dist/types/types";

const hono = new Hono();

const UserCreateSchema = z.object({
  username: z.string(),
  email: z.string().email(),
});

const db = (c: Context<string, Environment, any>) =>
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

app.get("/users/:id", async (c) => {
  const id = c.req.param("id");
  const prisma = db(c);

  const user = await prisma.user.findUnique({ where: { id } });
  return c.json(user);
});

export default app;
