import { observer } from 'mobx-react-lite';
import Error404Page from '../../pages/Error404Page';
import useStore from '../../store/useStores';
import { Navigate } from 'react-router';

const PrivatePage = (props: any) => {
  const authStore = useStore().authStore;
  return authStore.userInfo ? props.children : <Navigate to="/login" />;
};

export default PrivatePage;
