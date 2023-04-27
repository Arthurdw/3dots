import { Context } from "hono";

interface User {
  id: string;
  username: string;
  email: string;
  createdAt: string;
  updatedAt: string;
}

export const getAuthUser = (c: Context): User => c.get("jwtPayload").user;
