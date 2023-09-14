import { Snackbar } from '@mui/material';
import { observer } from 'mobx-react-lite';
import { forwardRef, useEffect, useState } from 'react';
import MuiAlert, { AlertProps } from '@mui/material/Alert';
import useStore from '../../store/useStores';
import alertStore from '../../store/component/alertStore.ts';

const Alert = forwardRef<HTMLDivElement, AlertProps>(
  function Alert(props, ref) {
    return <MuiAlert elevation={4} ref={ref} variant="filled" {...props} />;
  }
);

const CustomAlert = () => {
  const alertStore: alertStore = useStore().alertStore;

  const handleClose = (
    event?: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    return reason !== 'clickaway' && alertStore.clearAlert();
  };

  return (
    <Snackbar
      open={alertStore.alertProps.open}
      autoHideDuration={3000}
      onClose={handleClose}
      anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
      <Alert
        onClose={handleClose}
        severity={
          alertStore.alertProps.state ? alertStore.alertProps.state : 'error'
        }>
        {alertStore.alertProps.message
          ? alertStore.alertProps.message
          : alertStore.alertProps.state}
      </Alert>
    </Snackbar>
  );
};

export default observer(CustomAlert);
