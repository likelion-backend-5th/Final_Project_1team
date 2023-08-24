import { createContext, useContext } from 'react';
import rootStore from '../store/rootStore';

export default () => {
  const rootstore = new rootStore();
  const context = createContext(rootstore);
  return useContext(context);
};
