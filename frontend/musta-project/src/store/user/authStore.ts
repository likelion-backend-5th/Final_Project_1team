import { makeAutoObservable } from 'mobx';
import authRepository from '../../reppository/authRepository';
import { removeRefershToken } from '../../uitls/cookies';
import { AxiosResponse } from 'axios';

type userInfo = {
  username: string;
  image_url: string;
  nickname: string;
  apiId: string;
  zipcode: string;
  city: string;
  address: string;
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
      })
      .catch((error: AxiosResponse) => Promise.reject(error));
  };

  logout = async () => {
    this.userInfo = undefined;

    localStorage.removeItem('accessToken');
    removeRefershToken();
  };
}
