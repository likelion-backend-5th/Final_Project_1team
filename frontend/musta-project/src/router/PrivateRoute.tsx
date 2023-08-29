import { Navigate } from 'react-router-dom';
import { RouteType } from './IRoute';
import Alert from '@mui/material/Alert';

export default ({ authentication, component }: RouteType) => {
  return authentication ? (
    { component }
  ) : (
    <Navigate
      to="/"
      {...(<Alert severity="error">접근할 수 없는 페이지입니다.</Alert>)}
    />
  );
};
