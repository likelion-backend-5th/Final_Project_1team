// export type Status = 'ACTIVE' | 'DELETED';
export const status = ['ACTIVE', 'DELETED'] as const;
export type Status = (typeof status)[number];
// export type ArticleStatus = 'LIVE' | 'EXPIRED';
export const articleStatus = ['LIVE', 'EXPIRED'] as const;
export type ArticleStatus = (typeof articleStatus)[number];

export const articleInputError = [
  '제목을 비워둘 수 없습니다.',
  '상세 내용을 비워둘 수 없습니다.',
  '가격은 음수로 설정할 수 없습니다.',
  '이미지 갯수는 최대 5개까지만 가능합니다.',
  '',
] as const;
export type ArticleInputError = (typeof articleInputError)[number];

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
  apiId: string;
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
    public apiId: string,
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

export function checkArticleInputValidation(
  title: string,
  description: string,
  price: number
) {
  let str = '';

  if (!title) {
    str = str.concat('', '제목을 비워둘 수 없습니다.');
  }

  if (!description) {
    str = str.concat('\n', '상세 내용을 비워둘 수 없습니다.');
  }

  if (price < 0) {
    str = str.concat('\n', '가격은 음수로 설정할 수 없습니다.');
  }

  return str;
}
