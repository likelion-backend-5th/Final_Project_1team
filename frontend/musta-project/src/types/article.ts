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

export class ArticleImpl implements Article {
  constructor(
    public id: string,
    public title: string,
    public description: string,
    public username: string,
    public thumbnail: string,
    public status: Status,
    public articleStatus: ArticleStatus,
    public createdDate: string
  ) {}
}

export const ofArticleImpl = (dto) => {
  return new ArticleImpl(
    dto.apiId,
    dto.title,
    dto.description,
    dto.username,
    dto.thumbnail,
    dto.status,
    dto.articleStatus,
    dto.createdDate
  );
};

export interface ArticlePaginationData {
  currentPage: number;
  totalPage: number;
  currentPageItemCount: number;
  contents: Article[];
}
