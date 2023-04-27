import { Context, Env } from "hono";
import { infer, ZodType } from "zod";

interface ZData<T extends ZodType<any, any, any>> {
  in: { json: T["_output"] };
  out: { json: T["_output"] };
}

type ZCtx<T extends ZodType<any, any, any>> = Context<
  Env,
  any,
  ZData<T>
>;
