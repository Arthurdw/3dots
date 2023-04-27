import { Context } from "hono";

export const getAuthUser = (c: Context): User => c.get("jwtPayload").user;
