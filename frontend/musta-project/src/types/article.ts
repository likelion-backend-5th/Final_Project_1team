export type Status = 'ACTIVE' | 'DELETED';
export type ArticleStatus = 'LIVE' | 'EXPIRED';

export function getChipColorByArticleStatus(articleStatus: ArticleStatus) {
  if (articleStatus === 'LIVE') {
    return 'primary';
  }

  if (articleStatus === 'EXPIRED') {
    return 'warning';
  }

  return 'secondary';
}

export interface Article {
  id: string;
  title: string;
  description: string;
  username: string;
  thumbnail: string;
  status: Status;
  articleStatus: ArticleStatus;
  createdDate: string;
}

export interface ArticlePaginationData {
  currentPage: number;
  totalPage: number;
  currentPageItemCount: number;
  contents: Article[];
}
