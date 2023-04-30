import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import { StockEndpoints, StockEndpointType } from "../utils/stock-endpoints";
import {db} from "../utils/db";

const CACHE_TTL = 60; // One minute

export default async (c: Context) => {
    const symbol = c.req.param("id");
    const prisma = db(c);
    const stockFetch = (endpoint: StockEndpointType) =>
        cachedFetch(endpoint(symbol, c.env.STOCKS_API_TOKEN), CACHE_TTL);

    let [quote, {stockName}] = await Promise.all([
        stockFetch(StockEndpoints.QUOTE),
        prisma.picks.findFirst({
            where: {
                stock: symbol,
            },
            select: {
                stockName: true,
            }
        })
    ]);

    if (!stockName) {
        let {stockName : name} = await prisma.followedStocks.findFirst({
            where: {
                stock: symbol,
            },
            select: {
                stockName: true,
            }
        })
        stockName = name;
    }

    if (!stockName) {
        let data = await stockFetch(StockEndpoints.OVERVIEW);
        let overviewData: OverviewData = await data.json();
        stockName = overviewData.Name;
    }

    const quoteData: QuoteData = await quote.json();

    return c.json({
        stockName,
        symbol,
        price: parseFloat(quoteData["Global Quote"]["05. price"]),
    });
};
