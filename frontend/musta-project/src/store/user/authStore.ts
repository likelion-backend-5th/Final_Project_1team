import { makeAutoObservable } from 'mobx';
import authRepository from '../../reppository/authRepository';
import { removeRefershToken } from '../../uitls/cookies';

type userInfo = {
  username: string;
  image_url: string;
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
    const res = await authRepository
      .findUserInfo()
      .then((res) => {
        this.userInfo = res.data;
      })
      .catch((error) => Promise.reject(error));
  };

  logout = async () => {
    this.userInfo = undefined;

    localStorage.removeItem('accessToken');
    removeRefershToken();
  };
}
