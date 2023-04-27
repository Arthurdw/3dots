export class StockEndpoints {
  public static readonly BASE_URL = "https://www.alphavantage.co/query";

  public static readonly OVERVIEW = this.make(`${this.BASE_URL}?function=OVERVIEW`);
  public static readonly INTRADAY = this.make(`${this.BASE_URL}?function=TIME_SERIES_INTRADAY&interval=5min`);
  public static readonly QUOTE = this.make(`${this.BASE_URL}?function=GLOBAL_QUOTE`);

  public static readonly type = this.make("");

  private static make(baseUrl: string) {
    return (symbol: string, apiKey: string) =>
      `${baseUrl}&symbol=${symbol}&apikey=${apiKey}`;
  }
}

export type StockEndpointType = typeof StockEndpoints.type; // All endpoints have the same type
