import { makeAutoObservable } from 'mobx';
import authRepository from '../../reppository/authRepository';
import { removeRefershToken } from '../../uitls/cookies';
import { AxiosResponse } from 'axios';

type userInfo = {
  username: string;
  image_url: string;
  nickname: string;
  email: string;
  apiId: string;
  zipcode: string;
  city: string;
  address: string;
  role: string[];
};

export default class authStore {
  constructor() {
    makeAutoObservable(this);
  }

  userInfo?: userInfo = undefined;

  authRepository = authRepository;

  findUserInfo = async () => {
    return await authRepository
      .findUserInfo()
      .then((res: AxiosResponse) => {
        this.userInfo = res.data;
        console.log(this.userInfo?.role);
        if (this.userInfo?.role.includes('ROLE_USER')) {
          console.log('User 역할이 있습니다.');
        } else {
          console.log('User 역할이 없습니다.');
        }
      })
      .catch((error: AxiosResponse) => Promise.reject(error));
  };

  logout = async () => {
    this.userInfo = undefined;

    localStorage.removeItem('accessToken');
    removeRefershToken();
  };
}
