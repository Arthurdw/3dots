import {z} from "zod";

export const UserChangeDetails = z.object({ username: z.string().min(3).max(20) });
