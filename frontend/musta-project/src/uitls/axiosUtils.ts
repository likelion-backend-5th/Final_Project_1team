import axiosInstance from '../common/axiosInstance';

export default class axiosUtils {
  static get = (url: string, param?: any | null): any => {
    return axiosInstance.request({
      method: 'get',
      url: '/api' + url,
      params: param,
      paramsSerializer: (param) => {
        const params = new URLSearchParams();
        for (const key in param) {
          params.append(key, param[key]);
        }
        return params.toString();
      },
    });
  };

  static post = (url: string, data?: any): any => {
    return axiosInstance.request({
      method: 'post',
      url: '/api' + url,
      data: data,
    });
  };

  static patch = (url: string, data?: any): any => {
    return axiosInstance.request({
      method: 'patch',
      url: '/api' + url,
      data: data,
    });
  };

  static put = (url: string, data?: any): any => {
    return axiosInstance.request({
      method: 'put',
      url: '/api' + url,
      data: data,
    });
  };

  static delete = (url: string, data?: any): any => {
    return axiosInstance.request({
      method: 'delete',
      url: '/api' + url,
      data: data,
    });
  };

  static oauth2Login = (url: string, data?: any): any => {
    return axiosInstance.request({
      method: 'post',
      url: url,
      params: data,
      paramsSerializer: (param) => {
        const params = new URLSearchParams();
        for (const key in param) {
          params.append(key, param[key]);
        }
        return params.toString();
      },
      data: data,
    });
  };
}
