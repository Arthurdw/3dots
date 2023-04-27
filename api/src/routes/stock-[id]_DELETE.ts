// "/stock/:id" DELETE route
import {Context} from "hono";

export default async (c: Context) => {
    const symbol = c.req.param("id");

}
