import { makeAutoObservable } from 'mobx';

export interface alertProps {
  open: boolean;
  state?: 'success' | 'info' | 'warning' | 'error';
  message?: string;
}

export interface alertState {
  state?: 'success' | 'info' | 'warning' | 'error';
  message?: string;
}

export default class alertStore {
  constructor() {
    makeAutoObservable(this);
  }

  alertProps: alertProps = {
    open: false,
    state: 'success',
  };

  openAlert = (state: alertState) => {
    this.alertProps = { ...state, open: true };
  };

  clearAlert = () => {
    this.alertProps = { open: false, message: undefined, state: undefined };
  };
}
