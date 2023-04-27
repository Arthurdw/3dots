import { Context } from "hono";

const makeNewsUrl = (token: string) =>
  `https://api.marketaux.com/v1/news/all?api_token=${token}&domains=fool.com,economist.com`;

const makeNewsRequest = async (
  baseUrl: string,
  page: number
): Promise<NewsResponse> => {
  const url = new URL(`${baseUrl}&page=${page}`);
  const response = await fetch(url.toString(), {
    cf: {
      cacheTtl: 60 * 60,
      cacheEverything: true,
      cacheKey: url.toString(),
    },
  });
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
  token: string,
  pages: number
): Promise<NewsService[]> => {
  const url = makeNewsUrl(token);
  const data = await Promise.all(
    Array.from({ length: pages }, (_, i) => i + 1).map((page) => {
      return makeNewsRequest(url, page);
    })
  );

  const flattenData = data.flatMap((d) => d.data);
  return buildNewsList(flattenData);
};

export default async (c: Context) => {
  const url = makeNewsUrl(c.env.NEWS_API_TOKEN);
  const data = await getDataOfPages(c.env.NEWS_API_TOKEN, 1);
  return c.json(data);
};
