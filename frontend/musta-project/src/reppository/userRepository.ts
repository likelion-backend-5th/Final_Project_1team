import axiosUtils from '../uitls/axiosUtils';

class userRepsoitory {
  url = '/auth';

  login = (data: any) => {
    return axiosUtils.post(this.url + '/login', data);
  };

  signup = (data: any) => {
    return axiosUtils.post('/user/signup', data);
  };


  oauthSignup = (data: any) => {
    return axiosUtils.post('/user/oauth/signup', data);
  };

  changePassword = (passwordForm: {
    password: string;
    newPassword: string;
    newPasswordCheck: string;
  }) => {
    return axiosUtils.patch('/user/password', passwordForm);
  };
}

export default new userRepsoitory();
