import { makeAutoObservable } from 'mobx';
import userRepository from '../../reppository/userRepository';
import { ChangeEvent } from 'react';
import _ from 'lodash';
import { AxiosResponse, ResponseType } from 'axios';
import { useNavigate, useNavigation, useRoutes } from 'react-router-dom';

interface userInfo {
  username: string;
  accessToken: string;
}

type loginform = {
  username: string;
  password: string;
};

type signupform = {
  username: string;
  password: string;
  checkPassword: string;
  email: string;
  phone: string;
  address?: { zipcode: string; city: string; street?: string };
};

export default class userStore {
  constructor() {
    makeAutoObservable(this);
  }
  userInfo: userInfo = {
    username: '',
    accessToken: '',
  };

  userAddress = { zipcode: '', city: '', street: '' };

  userRepository = userRepository;

  getUserInfo = async () => {
    this.userInfo;
  };

  logout = async () => {
    this.userInfo = {
      username: '',
      accessToken: '',
    };

    localStorage.removeItem('authentication');
  };

  login = async (data: FormData) => {
    const username: string | undefined = data.get('id')?.toString();
    const password: string | undefined = data.get('password')?.toString();
    if (username == undefined || password == undefined) {
      return;
    }
    const loginform: loginform = {
      username: username,
      password: password,
    };

    const res = await userRepository
      .login(loginform)
      .then((data: any) => {
        return data;
      })
      .catch(() => {
        console.error();
      });

    if (res.status.include(2)) {
      localStorage.setItem('authentication', res.data.token);
      this.userInfo = {
        ...this.userInfo,
        username: res.data.username,
        accessToken: res.data.accessToken,
      };
    }
  };

  handleSignup = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);
    const signupform: signupform = {
      username: (data.get('id') as string).toLowerCase(),
      password: data.get('password') as string,
      checkPassword: data.get('check_password') as string,
      email: data.get('email') as string,
      phone: data.get('phone') as string,
    };

    if (
      this.userAddress.city != undefined &&
      this.userAddress.zipcode != undefined
    ) {
      signupform.address = {
        city: this.userAddress.city,
        zipcode: this.userAddress.zipcode,
        street: this.userAddress.street,
      };
    }

    const res = await userRepository
      .signup(signupform)
      .then((res: AxiosResponse) => {
        if (res.status < 300 && res.status >= 200) {
          const navigate = useNavigate();
          navigate('');
        }
      })
      .catch((res: AxiosResponse) => {});
  };

  setAddress = ({ zipcode, city }: { zipcode: string; city: string }) => {
    this.userAddress = {
      ...this.userAddress,
      zipcode: zipcode,
      city: city,
    };
  };

  setStreet = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    this.userAddress.street =
      this.userAddress.street != null ? e.target.value : '';
  };

  isDisabled = () => {
    return !(
      this.userAddress.zipcode.length > 0 && this.userAddress.city.length > 0
    );
  };
}
