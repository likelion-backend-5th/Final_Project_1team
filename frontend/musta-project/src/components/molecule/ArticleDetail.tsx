import { useState } from 'react';
import { Article, ArticleStatus, Status } from '../../types/article.ts';

export default function ArticleDefault() {
  const [article, setArticle] = useState<Article>({
    id: '1111',
    title: 'test-1',
    description: 'test-1',
    username: 'user1',
    thumbnail: '',
    status: Status.ACTIVE,
    articleStatus: ArticleStatus.EXPIRED,
    createdDate: '2023-08-23 24:10:00',
  });

  return <></>;
}
