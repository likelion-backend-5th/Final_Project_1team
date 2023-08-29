import { createContext, useContext } from 'react';
import userStore from './user/userStore';
import authStore from './user/authStore';

class rootStore {
  constructor() {
    this.userStore = new userStore();
    this.authStore = new authStore();
  }
  userStore: userStore;
  authStore: authStore;
}

const rootstore = new rootStore();
const context = createContext(rootstore);
const useStore = () => {
  return useContext(context);
};

export default useStore;
