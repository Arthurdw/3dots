import { Context } from "hono";
import { PrismaClient } from "@prisma/client/edge";

export const db = (c: Context) =>
  new PrismaClient({
    datasources: {
      db: {
        url: c.env.DATABASE_URL,
      },
    },
  });