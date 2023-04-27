interface NewsService {
    title: string;
    snippet: string;
    url: string;
    image_url: string;
    published_at: string;
    source: string;
    similar?: NewsService[];
}

interface NewsResponse {
    data: NewsService[];
}
