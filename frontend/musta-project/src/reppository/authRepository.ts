import axiosUtils from '../uitls/axiosUtils';

class authRepository {
  url = '/user';
  findUserInfo = async () => {
    return axiosUtils.get(this.url + '/info');
  };
}

export default new authRepository();
