// export type Status = 'ACTIVE' | 'DELETED';
export const status = ['ACTIVE', 'DELETED'] as const;
export type Status = (typeof status)[number];
// export type ArticleStatus = 'LIVE' | 'EXPIRED';
export const articleStatus = ['LIVE', 'EXPIRED'] as const;
export type ArticleStatus = (typeof articleStatus)[number];

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
  price: number;
  images: { apiId: string; refApiId: string; fullPath: string }[];
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
    public createdDate: string,
    public price: number,
    public images: { apiId: string; refApiId: string; fullPath: string }[]
  ) {}
}

export const ofArticleImpl = (dto: {
  apiId: string;
  title: string;
  description: string;
  username: string;
  thumbnail: string;
  status: string;
  articleStatus: string;
  createdDate: string;
  price: number;
  images: { apiId: string; refApiId: string; fullPath: string }[];
}) => {
  return new ArticleImpl(
    dto.apiId,
    dto.title,
    dto.description,
    dto.username,
    dto.thumbnail,
    dto.status as Status,
    dto.articleStatus as ArticleStatus,
    dto.createdDate,
    dto.price,
    dto.images
  );
};

export interface ArticlePaginationData {
  currentPage: number;
  totalPage: number;
  currentPageItemCount: number;
  contents: Article[];
}
