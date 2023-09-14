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
        console.debug(this.userInfo?.role);
      })
      .catch((res) => {
        if (res.data.code === 'A001') {
          authRepository
            .requestAccessToken()
            .then((refreshRes) => {
              localStorage.setItem('accessToken', refreshRes.data.accessToken);
              this.findUserInfo();
            })
            .catch((err: any) => {
              console.debug(err);
            });
        }
      });
  };

  logout = async () => {
    this.userInfo = undefined;

    localStorage.removeItem('accessToken');
    removeRefershToken();
  };
}
