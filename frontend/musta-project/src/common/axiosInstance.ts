import axios from 'axios';

const AUTH_TOKEN = `Bearer `;

const baseUrl = import.meta.env.VITE_API;

console.log(baseUrl);

const axiosInstance = axios.create({ baseURL: baseUrl });

axiosInstance.defaults.headers.common['Authorization'] = AUTH_TOKEN;
axiosInstance.defaults.headers.get['content-Type'] = 'application/json';
axiosInstance.defaults.headers.post['content-Type'] = 'application/json';
axiosInstance.defaults.headers.put['content-Type'] = 'application/json';
axiosInstance.defaults.headers.delete['content-Type'] = 'application/json';
axiosInstance.defaults.withCredentials = true;
axiosInstance.interceptors.request.use(
  (config) => {
    config.headers['Authorization'] = localStorage.getItem('token');
    return config;
  },
  (err) => {
    console.log('axios error');
    return Promise.reject(err);
  }
);
axiosInstance.interceptors.response.use(
  (res) => {
    console.log(res);
    return res;
  },
  (err) => {
    return Promise.reject(err);
  }
);

export default axiosInstance;
