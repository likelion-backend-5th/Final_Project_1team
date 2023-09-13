import axiosUtils from '../uitls/axiosUtils';

class authRepository {
  url = '/user';
  findUserInfo = async () => {
    return axiosUtils.get(this.url + '/info');
  };

  requestAccessToken = async () => {
    return axiosUtils.post('/auth/token/refresh');
  };
}

export default new authRepository();
