export const cachedFetch = async (url: string, time = 60 * 60) =>
  await fetch(url, {
    cf: {
      cacheTtl: time,
      cacheEverything: true,
      cacheKey: url,
    },
  });