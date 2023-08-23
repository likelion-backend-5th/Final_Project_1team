import { action, makeObservable, observable } from 'mobx';
import { Article, ArticleStatus, Status } from '../types/article.ts';

class ArticleData {
  id: string;
  title: string;
  description: string;
  username: string;
  thumbnail: string;
  status: Status;
  articleStatus: ArticleStatus;
  createdDate: string;

  constructor(
    id: string,
    title: string,
    description: string,
    username: string,
    thumbnail: string,
    status: Status,
    articleStatus: ArticleStatus,
    createdDate: string
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.username = username;
    this.thumbnail = thumbnail;
    this.status = status;
    this.articleStatus = articleStatus;
    this.createdDate = createdDate;
  }
}

export class ArticleStore {
  _articles: Article[] = [];

  _currentPage = 1;
  _totalPage = 1;
  _currentPageItemCount = 10;
  _pageItemLimit = 10;

  constructor() {
    makeObservable(this, {
      _articles: observable,
      _totalPage: observable,
      _currentPage: observable,
      _pageItemLimit: observable,
      _currentPageItemCount: observable,
      articles: action,
      totalPage: action,
      currentPage: action,
      pageItemLimit: action,
      currentPageItemCount: action,
    });
  }

  set currentPage(value: number) {
    this._currentPage = value;
  }

  set totalPage(value: number) {
    this._totalPage = value;
  }

  set currentPageItemCount(value: number) {
    this._currentPageItemCount = value;
  }

  set pageItemLimit(value: number) {
    this._pageItemLimit = value;
  }

  set articles(value: Article[]) {
    this._articles = value;
  }
}
