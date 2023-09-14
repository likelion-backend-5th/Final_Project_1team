import { createContext, useContext } from 'react';
import userStore from './user/userStore';
import authStore from './user/authStore';
import alertStore from './component/AlertStore';

class rootStore {
  constructor() {
    this.userStore = new userStore();
    this.authStore = new authStore();
    this.alertStore = new alertStore();
  }
  userStore: userStore;
  authStore: authStore;
  alertStore: alertStore;
}

const rootstore = new rootStore();
const context = createContext(rootstore);
const useStore = () => {
  return useContext(context);
};

export default useStore;
