import axios from 'axios';

const AUTH_TOKEN = `Bearer ${toekn}`;

const axiosInstance = axios.create({
  headers: {
    'content-Type': 'application/json',
  },
  baseURL: import.meta.env.REACT_APP_API,
});
axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
axios.defaults.headers.get['content-Type'] = 'application/json';

axiosInstance.interceptors.request.use(
  (config) => {
    config.headers['Authorization'] = localStorage.getItem('token');
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);
