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

  changeImage(raw: any) {
    return axiosUtils.patch('/user/image', raw);
  }

  changePassword = (passwordForm: {
    password: string;
    newPassword: string;
    newPasswordCheck: string;
  }) => {
    return axiosUtils.patch('/user/password', passwordForm);
  };

  changeEmail(data: any) {
    return axiosUtils.patch('/user/email', data);
  }
  changeAddress(userAddress: {
    zipcode: string;
    city: string;
    street: string;
  }) {
    return axiosUtils.patch('/user/address', userAddress);
  }
}

export default new userRepsoitory();
