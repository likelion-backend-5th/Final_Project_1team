import { makeAutoObservable } from 'mobx';

interface userInfo {
  username: string;
  accessToken: string;
  user: string;
}

export default class userStore {
  constructor() {
    makeAutoObservable(this);
  }
  userInfo: userInfo = {
    username: '',
    accessToken: '',
    user: '',
  };

  getUserInfo = async () => {
    this.userInfo;
  };

  logout = async () => {
    this.userInfo = {
      username: '',
      accessToken: '',
      user: '',
    };
  };
}
