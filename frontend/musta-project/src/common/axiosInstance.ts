import axios from 'axios';
import _ from 'lodash';

const AUTH_TOKEN = `Bearer `;

axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
axios.defaults.headers.get['content-Type'] = 'application/json';
axios.defaults.baseURL = import.meta.env.REACT_APP_API;
axios.defaults.withCredentials = true;
axios.interceptors.request.use(
  (config) => {
    config.headers['Authorization'] = localStorage.getItem('token');
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);
axios.interceptors.response.use((res) => {
  if (_.isUndefined(res || _.isNull(res))) {
    console.error(res);
  }
  return res;
});
