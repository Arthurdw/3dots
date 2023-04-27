import { Context, Next } from "hono";
import { jwt } from "hono/jwt";

export const requiresAuth = (c: Context, next: Next) => {
  return jwt({ secret: c.env.JWT_SECRET })(c, next);
};
