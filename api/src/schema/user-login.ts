import {z} from "zod";

export const UserLogin = z.object({ token: z.string() });
