export const Status = {
  ACTIVE: 'ACTIVE',
  DELETED: 'DELETED',
} as const;
type Status = (typeof Status)[keyof typeof Status];

export const ArticleStatus = {
  LIVE: 'LIVE',
  EXPIRED: 'EXPIRED',
};
type ArticleStatus = (typeof ArticleStatus)[keyof typeof ArticleStatus];

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
