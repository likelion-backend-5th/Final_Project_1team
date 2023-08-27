import { GET, POST, PUT } from "./fetch-auth-action";

const createTokenHeader = (token: string) => {
    return {
        headers: {
            Authorization: "Bearer " + token,
        },
    };
};

const createMultipartTokenHeader = (token: string) => {
    return {
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: "Bearer " + token,
        },
    };
};

const calculateRemainingTime = (expirationTime: number) => {
    const currentTime = new Date().getTime();
    const adjExpirationTime = new Date(expirationTime).getTime();
    return adjExpirationTime - currentTime;
};

export const loginTokenHandler = (
    accessToken: string,
    expirationTime: number
) => {
    localStorage.setItem("token", accessToken);
    localStorage.setItem("expirationTime", String(expirationTime));

    return calculateRemainingTime(expirationTime);
};

export const retrieveStoredToken = () => {
    const storedToken = localStorage.getItem("token");
    const storedExpirationDate = localStorage.getItem("expirationTime") || "0";

    const remaingTime = calculateRemainingTime(+storedExpirationDate);

    if (remaingTime <= 1000) {
        localStorage.removeItem("token");
        localStorage.removeItem("expirationTime");
        return null;
    }

    return {
        token: storedToken,
        duration: remaingTime,
    };
};

export const signupActionHandler = (
    username: string,
    password: string,
    passwordCheck: string,
    nickname: string
) => {
    const URL = "user/auth/signup";
    const signupObjcect = { username, password, passwordCheck, nickname };
    return POST(URL, signupObjcect, {});
};

export const loginActionHandler = (username: string, password: string) => {
    const URL = "user/auth/login";
    const loginObject = { username, password };
    return POST(URL, loginObject, {});
};

export const getUserActionHandler = (token: string) => {
    const URL = "/user/my/profile";
    return GET(URL, createTokenHeader(token));
};

export const logoutActionHandler = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("expirationTime");
};

export const getArticleOrderHandler = (
    token: string,
    articleApiId: string,
    pageParam: number | undefined,
    limitParam: number | undefined
) => {
    const queryParams: Record<string, string> = {};

    if (pageParam !== undefined) {
        queryParams.pageParam = pageParam.toString();
    }

    if (limitParam !== undefined) {
        queryParams.limitParam = limitParam.toString();
    }

    const queryString = getQueryString(queryParams);

    const URL = `/api/articles/${articleApiId}/order?${queryString}`;
    return GET(URL, createTokenHeader(token));
};

export const getSellOrderHandler = (
    token: string,
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

    const URL = `/api/order/sell?${queryString}`;
    return GET(URL, createTokenHeader(token));
};

export const getConsumerOrderHandler = (
    token: string,
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

    const URL = `/api/order/consume?${queryString}`;
    return GET(URL, createTokenHeader(token));
};

export const getOrderHandler = (
    token: string,
    articleApiId: string | undefined,
    orderApiId: string | undefined,
) => {
    const URL = `/api/articles/${articleApiId}/order/${orderApiId}`;
    return GET(URL, createTokenHeader(token));
};

function getQueryString(queryParams: Record<string, string>) {
    // queryParams 객체를 사용하여 URL 파라미터 문자열 생성
    const queryString = Object.keys(queryParams)
        .map(key => `${key}=${queryParams[key]}`)
        .join('&');
    return queryString;
}