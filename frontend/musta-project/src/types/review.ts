export interface Review {
  apiId: string;
  username: string;
  content: string;
  point: number;
  createdAt: string;
  reviewStatus: string;
  images: { apiId: string; refApiId: string; fullPath: string }[];
}
