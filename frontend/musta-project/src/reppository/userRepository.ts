import axiosUtils from '../uitls/axiosUtils';

class userRepsoitory {
  url = '/auth';

  login = (data: any) => {
    return axiosUtils.post(this.url + '/login', data);
  };

  signup = (data: any) => {
    return axiosUtils.post('/user/signup', data);
  };
}

export default new userRepsoitory();
