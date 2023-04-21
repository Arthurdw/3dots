import { PrismaClient } from "@prisma/client/edge";
import { Context, Hono } from "hono";
import { poweredBy } from "hono/powered-by";
import { prettyJSON } from "hono/pretty-json";
import { logger } from "hono/logger";
import { etag } from "hono/etag";
import { Environment } from "hono/dist/types/types";

const hono = new Hono();

const db = (c: Context<string, Environment, null>) =>
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

app.get("/", (c) => {
  return c.text("Hello Hono!");
});

app.get("/users", async (c) => {
  const prisma = db(c);

  const users = await prisma.user.findMany();

  return c.json(users);
});

export default app;
