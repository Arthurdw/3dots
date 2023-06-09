import { Hono } from "hono";
import { poweredBy } from "hono/powered-by";
import { prettyJSON } from "hono/pretty-json";
import { logger } from "hono/logger";
import { etag } from "hono/etag";
import { zValidator } from "@hono/zod-validator";
import { UserLogin } from "./schema/user-login";
import { UserChangeDetails } from "./schema/user-change-details";
import { requiresAuth } from "./middleware/requires-auth";
import userLoginOrRegister from "./routes/users_POST";
import userGetMe from "./routes/users-me_GET";
import userPutMe from "./routes/users-me_PUT";
import userStocks from "./routes/users-me-picked_GET";
import usersMeWorth from "./routes/users-me-worth";
import userGetFollowedStocks from "./routes/users-me-followed_GET";
import newsGet from "./routes/news_GET";
import stockGet from "./routes/stock-[id]_GET";
import stockBuy from "./routes/stock-[id]_POST";
import stockSell from "./routes/stock-[id]_DELETE";
import stockStatus from "./routes/stock-[id]-status_GET";
import stockWorth from "./routes/stock-[id]-worth_GET";
import searchStocks from "./routes/stocks_GET";
import followStock from "./routes/stock-[id]-follow_PUT";
import unfollowStock from "./routes/stock-[id]-follow_DELETE";

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
app.get("/users/me/worth", requiresAuth, usersMeWorth);
app.get("/users/me/picked", requiresAuth, userStocks);
app.get("/users/me/followed", requiresAuth, userGetFollowedStocks);
app.get("/news", requiresAuth, newsGet);
app.get("/stocks", requiresAuth, searchStocks);
app.get("/stock/:id", requiresAuth, stockGet);
app.post("/stock/:id", requiresAuth, stockBuy);
app.delete("/stock/:id", requiresAuth, stockSell);
app.get("/stock/:id/status", requiresAuth, stockStatus);
app.get("/stock/:id/worth", requiresAuth, stockWorth);
app.put("/stock/:id/follow", requiresAuth, followStock);
app.delete("/stock/:id/follow", requiresAuth, unfollowStock);
app.put(
    "/users/me",
    requiresAuth,
    zValidator("json", UserChangeDetails),
    userPutMe
);

export default hono;
