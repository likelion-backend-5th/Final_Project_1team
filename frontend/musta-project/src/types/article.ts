type status = 'ACTIVE' | 'DELETED';
type articleStatus = 'LIVE' | 'EXPIRED';

interface Article {
  id: string;
  title: string;
  description: string;
  username: string;
  thumbnail: string;
  status: status;
  articleStatus: articleStatus;
  createdDate: string;
}
