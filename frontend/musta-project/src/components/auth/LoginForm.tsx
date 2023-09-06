import { Copyright } from '@mui/icons-material';
import {
  Alert,
  Avatar,
  Box,
  Button,
  Grid,
  Link,
  TextField,
  Typography,
} from '@mui/material';
import useStores from '../../store/useStores';
import userStore from '../../store/user/userStore';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { observer } from 'mobx-react-lite';
import React, { useEffect, useState } from 'react';
import { GoogleLogin, useGoogleLogin } from '@react-oauth/google';
import axiosUtils from '../../uitls/axiosUtils';
import authStore from '../../store/user/authStore';
import { useNavigate, useNavigation } from 'react-router-dom';

const LoginForm = (props: any): JSX.Element => {
  const { userStore, authStore } = useStores();
  const [id, setId] = useState<String>('');
  const [password, setPassword] = useState<String>('');
  const navigate = useNavigate();
  const handleEvent = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);
    if (data.get('id') == null || data.get('password') == null) {
      console.log(data.get('id'));
      console.log(data.get('password'));
    }
    await userStore
      .login(data)
      .then(async () => {
        await authStore
          .findUserInfo()
          .then(() => {
            navigate('/');
          })
          .catch();
      })
      .catch();
  };

  const googleLogin = useGoogleLogin({
    scope: 'email profile',
    onSuccess: async (code) => {
      code = { ...code, state: 'success' };
      axiosUtils
        .oauth2Login('/login/oauth2/code/google', code)
        .then((res: any) => {
          console.log(res);
        });
    },
    onError: (res) => {
      console.error(res);
    },
    flow: 'auth-code',
  });

  useEffect(() => {
    setId('');
    setPassword('');
    return () => {
      setId('');
      setPassword('');
    };
  }, []);

  const handleId = (e: any) => {
    setId(e.target.value);
  };

  const handlePassword = (e: any) => {
    setPassword(e.target.value);
  };

  return (
    <Box
      sx={{
        my: 8,
        mx: 4,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}>
      <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
        <LockOutlinedIcon />
      </Avatar>
      <Typography component="h1" variant="h5">
        Sign in
      </Typography>
      <Box component="form" noValidate onSubmit={handleEvent} sx={{ mt: 1 }}>
        <TextField
          margin="normal"
          required
          fullWidth
          id="id"
          label="ID"
          name="id"
          autoComplete="id"
          autoFocus
          onChange={handleId}
          value={id}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          name="password"
          label="PASSWORD"
          type="password"
          id="password"
          autoComplete="current-password"
          onChange={handlePassword}
          value={password}
        />
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}>
          Sign In
        </Button>
        <Grid item sx={{ mb: 2 }}>
          <GoogleLogin size="large" ux_mode="popup" onSuccess={googleLogin} />
        </Grid>
        <Grid container>
          <Grid item xs>
            <Link href="/find/id" variant="body2">
              아이디 찾기
            </Link>
          </Grid>
          <Grid item xs>
            <Link href="/find/password" variant="body2">
              비밀번호 찾기
            </Link>
          </Grid>
          <Grid item xs>
            <Link href="/signup" variant="body2">
              회원가입
            </Link>
          </Grid>
        </Grid>
        <Grid container></Grid>
        <Copyright sx={{ mt: 5 }} />
      </Box>
    </Box>
  );
};

export default observer(LoginForm);
