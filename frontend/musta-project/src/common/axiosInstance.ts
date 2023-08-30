import axios from 'axios';

const AUTH_TOKEN = `Bearer `;

const axiosInstance = axios.create({ baseURL: import.meta.env.VITE_API });

axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
axios.defaults.headers.get['content-Type'] = 'application/json';
axios.defaults.headers.post['content-Type'] = 'application/json';
axios.defaults.headers.put['content-Type'] = 'application/json';
axios.defaults.headers.delete['content-Type'] = 'application/json';
axios.defaults.withCredentials = true;
axios.interceptors.request.use(
  (config) => {
    config.headers['Authorization'] = localStorage.getItem('token');
    return config;
  },
  (err) => {
    console.log('axios error');
    return Promise.reject(err);
  }
);
axios.interceptors.response.use(
  (res) => {
    console.log(res);
    return res;
  },
  (err) => {
    return Promise.reject(err);
  }
);

export default axiosInstance;
