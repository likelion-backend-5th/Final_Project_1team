import { makeAutoObservable } from 'mobx';

export default class authStore {
  constructor() {
    makeAutoObservable(this);
  }
  
}
