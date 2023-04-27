import { Hono } from "hono";
import { poweredBy } from "hono/powered-by";
import { prettyJSON } from "hono/pretty-json";
import { logger } from "hono/logger";
import { etag } from "hono/etag";
import { zValidator } from "@hono/zod-validator";
import { UserLogin } from "./schema/user-login";
import userLoginOrRegister from "./routes/users_POST";
import userGetMe from "./routes/users-me_GET";
import { requiresAuth } from "./middleware/requires-auth";

const honoClient = new Hono();
const hono = honoClient.basePath("/api");
const app = hono.basePath("/v1");

hono.use("*", poweredBy(), prettyJSON(), logger(), etag());
hono.get("/", (c) =>
  c.text(
    "Welcome to the 3dots API! All endpoints are scoped under the /v1 path."
  )
);

app.post("/users", zValidator("json", UserLogin), userLoginOrRegister);
app.get("/users/me", requiresAuth, userGetMe);
export default hono;
