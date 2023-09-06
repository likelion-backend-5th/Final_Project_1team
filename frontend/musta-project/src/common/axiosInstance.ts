import axios, { CustomParamsSerializer, ParamsSerializerOptions } from 'axios';

const AUTH_TOKEN = `Bearer `;

const baseUrl = import.meta.env.VITE_API;

console.log(baseUrl);

const axiosInstance = axios.create({ baseURL: baseUrl });
axios.defaults.paramsSerializer = (params: Record<string, any>) => {
  const param = new URLSearchParams();
  for (const key in params) {
    param.append(key, param.get(key) as string);
  }
  return param.toString();
};
axiosInstance.defaults.headers.common['Authorization'] = AUTH_TOKEN;
axiosInstance.defaults.headers.get['content-Type'] = 'application/json';
axiosInstance.defaults.headers.post['content-Type'] = 'application/json';
axiosInstance.defaults.headers.put['content-Type'] = 'application/json';
axiosInstance.defaults.paramsSerializer = (params: Record<string, any>) => {
  const param = new URLSearchParams();
  for (const key in params) {
    param.append(key, param.get(key) as string);
  }
  return param.toString();
};
axiosInstance.defaults.headers.delete['content-Type'] = 'application/json';
axiosInstance.defaults.withCredentials = true;
axiosInstance.interceptors.request.use(
  (config) => {
    config.headers['Authorization'] = localStorage.getItem('accessToken');
    return config;
  },
  (err) => {
    console.debug('axios error');
    return Promise.reject(err);
  }
);
axiosInstance.interceptors.response.use(
  (res) => {
    console.debug(res);
    return res;
  },
  (err) => {
    return Promise.reject(err);
  }
);

export default axiosInstance;
