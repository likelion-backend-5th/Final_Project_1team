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
// axiosInstance.defaults.headers.patch['content-Type'] = 'application/jsons';
axiosInstance.defaults.headers.delete['content-Type'] = 'application/json';
axiosInstance.defaults.paramsSerializer = (params: Record<string, any>) => {
  const param = new URLSearchParams();
  for (const key in params) {
    param.append(key, param.get(key) as string);
  }
  return param.toString();
};
axiosInstance.defaults.withCredentials = true;
axiosInstance.interceptors.request.use(
  (config) => {
    config.headers['Authorization'] = localStorage.getItem('accessToken');
    return config;
  },
  (err: any) => {
    return Promise.reject(err.response);
  }
);
axiosInstance.interceptors.response.use(
  (res: any) => {
    return res;
  },
  (err: any) => {
    return Promise.reject(err.response);
  }
);

export default axiosInstance;
