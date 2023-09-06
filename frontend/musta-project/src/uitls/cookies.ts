import { Cookies } from 'react-cookie';

const cookies = new Cookies();

export const removeRefershToken = () => {
  return cookies.remove('refresh_token');
};
