import authStore from './user/authStore';
import userStore from './user/userStore';

export default class rootStore {
  constructor() {
    this.userStore = new userStore();
    this.authStore = new authStore();
  }
  userStore: userStore;
  authStore: authStore;
}
