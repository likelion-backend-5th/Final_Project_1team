import {
  Box,
  Button,
  FormControl,
  Grid,
  TextField,
  Typography,
} from '@mui/material';
import { observer } from 'mobx-react-lite';
import { FormEvent, FormEventHandler, useEffect, useState } from 'react';
import useStore from '../../store/useStores';
import { useAlert } from '../hook/useAlert';
import { AxiosResponse } from 'axios';
import { useNavigate } from 'react-router';
import { Label } from '@mui/icons-material';

const ChangePasswordForm = () => {
  const userStore = useStore().userStore;
  const { openAlert } = useAlert();
  const navigate = useNavigate();

  const [password, setPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const [newPasswordCheck, setNewPasswordCheck] = useState<string>('');

  const passwordRegex = new RegExp(/^[a-zA-Z0-9!@#$%^&*]{8,24}$/);

  useEffect(() => {
    setPassword('');
    setNewPassword('');
    setNewPasswordCheck('');
  }, []);

  const handleEvent = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);

    await userStore
      .changePassword(data)
      .then((res: AxiosResponse) => {
        openAlert({ state: 'success', message: '비밀번호가 변경되었습니다.' });
        setPassword('');
        setNewPassword('');
        setNewPasswordCheck('');
      })
      .catch((res: AxiosResponse) => {
        openAlert({ state: 'error', message: res.data.message });
        setPassword('');
        setNewPassword('');
        setNewPasswordCheck('');
      });
  };

  return (
    <Grid sx={{ mt: 10, mx: 10 }} xs={9} height={'80vh'}>
      <Box component="form" onSubmit={handleEvent} margin="0 auto">
        <Typography fontSize={28}>비밀번호 변경</Typography>
        <TextField
          sx={{ mt: 3, mb: 2 }}
          required
          fullWidth
          error={!passwordRegex.test(password)}
          id="password"
          label="비밀번호"
          name="password"
          type="password"
          autoComplete="password"
          autoFocus
          onChange={(e) => {
            setPassword(e.target.value);
          }}
          value={password}
        />
        <TextField
          sx={{ mt: 3, mb: 2 }}
          required
          fullWidth
          error={!passwordRegex.test(newPassword)}
          id="newPassword"
          label="새 비밀번호"
          name="newPassword"
          type="password"
          autoComplete="newPassword"
          autoFocus
          onChange={(e) => {
            setNewPassword(e.target.value);
          }}
          value={newPassword}
        />
        <TextField
          sx={{ mt: 3, mb: 2 }}
          required
          fullWidth
          error={!passwordRegex.test(newPasswordCheck)}
          name="newPasswordCheck"
          label="새 비밀번호 확인"
          type="password"
          id="newPasswordCheck"
          autoComplete="newPasswordCheck"
          onChange={(e) => {
            setNewPasswordCheck(e.target.value);
          }}
          value={newPasswordCheck}
        />
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}>
          비밀번호 변경
        </Button>
      </Box>
    </Grid>
  );
};

export default observer(ChangePasswordForm);
