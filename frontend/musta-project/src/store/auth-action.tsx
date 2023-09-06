import { ArticleStatus } from '../types/article.ts';
import axiosUtils from '../uitls/axiosUtils.ts';

export const getArticleHandler = (articleApiId: any) => {
  const URL = `/articles/${articleApiId}`;
  return axiosUtils.get(URL);
};

export const getArticleOrderHandler = (
  articleApiId: string | undefined,
  orderStatus: string | undefined,
  sortOrder: string | undefined,
  page: number | undefined,
  limit: number | undefined
) => {
  const queryParams: Record<string, string> = {};

  if (page !== undefined) {
    queryParams.page = page.toString();
  }

  if (limit !== undefined) {
    queryParams.limit = limit.toString();
  }

  if (orderStatus !== undefined) {
    queryParams.orderStatus = orderStatus;
  }

  if (sortOrder !== undefined) {
    queryParams.sortOrder = sortOrder;
  }

  const queryString = getQueryString(queryParams);

  const URL = `/articles/${articleApiId}/order?${queryString}`;
  return axiosUtils.get(URL);
};

export const getSellOrderHandler = (
  orderStatus: string | undefined,
  searchText: string | undefined,
  sortOrder: string | undefined,
  page: number | undefined,
  limit: number | undefined
) => {
  const queryParams: Record<string, string> = {};

  if (orderStatus !== undefined) {
    queryParams.orderStatus = orderStatus;
  }

  if (searchText !== undefined) {
    queryParams.searchText = searchText;
  }

  if (sortOrder !== undefined) {
    queryParams.sortOrder = sortOrder;
  }

  if (page !== undefined) {
    queryParams.page = page.toString();
  }

  if (limit !== undefined) {
    queryParams.limit = limit.toString();
  }
  const queryString = getQueryString(queryParams);

  const URL = `/order/sell?${queryString}`;
  return axiosUtils.get(URL);
};

export const getConsumerOrderHandler = (
  orderStatus: string | undefined,
  searchText: string | undefined,
  sortOrder: string | undefined,
  page: number | undefined,
  limit: number | undefined
) => {
  const queryParams: Record<string, string> = {};

  if (orderStatus !== undefined) {
    queryParams.orderStatus = orderStatus;
  }

  if (searchText !== undefined) {
    queryParams.searchText = searchText;
  }

  if (sortOrder !== undefined) {
    queryParams.sortOrder = sortOrder;
  }

  if (page !== undefined) {
    queryParams.page = page.toString();
  }

  if (limit !== undefined) {
    queryParams.limit = limit.toString();
  }
  const queryString = getQueryString(queryParams);

  const URL = `/order/consume?${queryString}`;
  return axiosUtils.get(URL);
};

export const getOrderHandler = (
  articleApiId: string | undefined,
  orderApiId: string | undefined
) => {
  const URL = `/articles/${articleApiId}/order/${orderApiId}`;
  return axiosUtils.get(URL);
};

function getQueryString(queryParams: Record<string, string>) {
  // queryParams 객체를 사용하여 URL 파라미터 문자열 생성
  const queryString = Object.keys(queryParams)
    .map((key) => `${key}=${queryParams[key]}`)
    .join('&');
  return queryString;
}

export const postArticleHandler = (
  // token: string,
  title: string,
  description: string,
  price: number
) => {
  const URL = `/articles`;
  const articleRequestObject = { title, description, price };
  return axiosUtils.post(URL, articleRequestObject);
};

export const putArticleHandler = (
  title: string,
  description: string,
  apiId: string,
  articleStatus: ArticleStatus,
  images: string[],
  price: number
) => {
  const URL = `/articles`;
  const articleRequestObject = {
    title,
    description,
    apiId,
    articleStatus,
    images,
    price,
  };
  return axiosUtils.put(URL, articleRequestObject);
};

export const getChatroomHandler = () => {
  console.log('hello!');
  const url = '/chat/room';
  return axiosUtils.get(url);
};

export const getEachChatroomHandler = (chatroomId: string | undefined) => {
  const url = `/chat/room/${chatroomId}`;
  return axiosUtils.get(url);
};

export const createChatroom = (articleApiId: string | undefined) => {
  const url = '/chat/room';
  return axiosUtils.post(url, { articleApiId: articleApiId });
};

export const createOrder = (articleApiId: string | undefined) => {
  const url = `/articles/${articleApiId}/order`;
  return axiosUtils.post(url);
};
