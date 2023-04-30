import { Context } from "hono";
import { cachedFetch } from "../utils/cache";
import {db} from "../utils/db";

const makeNewsUrl = (token: string) =>
  `https://api.marketaux.com/v1/news/all?api_token=${token}&domains=fool.com,economist.com`;

const makeNewsRequest = async (
  baseUrl: string,
  page: number
): Promise<NewsResponse> => {
  const response = await cachedFetch(`${baseUrl}&page=${page}`);
  return (await response.json()) as NewsResponse;
};

const buildNewsList = (data: NewsService[]): NewsService[] => {
  const newData = [];

  for (const article of data) {
    const { title, snippet, url, image_url, source, published_at } = article;
    newData.push({ title, snippet, url, image_url, source, published_at });

    if (article.similar && article.similar.length > 0) {
      newData.push(...buildNewsList(article.similar));
    }
  }

  return newData;
};

const getDataOfPages = async (
    c: Context,
  token: string,
  pages: number
): Promise<NewsService[]> => {
  const url = makeNewsUrl(token);
  const data = await Promise.all(
    Array.from({ length: pages }, (_, i) => i + 1).map((page) => {
      try {
        return makeNewsRequest(url, page);
      } catch (err) {
        return null
      }
    })
  );

  let flattenData = data.flatMap((d) => d.data);

  const prisma = db(c);

  if (flattenData.length !== 0) {
    await prisma.newsCache.deleteMany({});
    await prisma.newsCache.createMany({
      data: flattenData.map((d) => ({
        title: d.title,
        snippet: d.snippet,
        url: d.url,
        imageUrl: d.image_url,
        source: d.source,
        publishedAt: d.published_at,
      })),
    });
  } else {
    const news = await prisma.newsCache.findMany({});
    flattenData = news.map((n) => ({
        title: n.title,
        snippet: n.snippet,
        url: n.url,
        image_url: n.imageUrl,
        source: n.source,
        published_at: n.publishedAt
    }));

  }
  return buildNewsList(flattenData);
};

export default async (c: Context) => {
  const data = await getDataOfPages(c, c.env.NEWS_API_TOKEN, 10);
  return c.json(data);
};
