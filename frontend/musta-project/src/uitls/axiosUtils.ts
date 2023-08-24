import axios,  from 'axios';

export default class axiosUtils {
  static get = (url: string, param: any | null): any => {
    return axios.request({
      method: 'get',
      url: '/api' + url,
      params: param,
    });
  };

  static post = (url: string, param: any | null): any => {
    return axios.request({
      method: 'post',
      url: '/api' + url,
      params: param,
    });
  };
  static put = (url: string, param: any | null): any => {
    return axios.request({
      method: 'put',
      url: '/api' + url,
      params: param,
    });
  };

  static delete = (url: string, param: any | null): any => {
    return axios.request({
      method: 'delete',
      url: '/api' + url,
      params: param,
    });
  };
}
