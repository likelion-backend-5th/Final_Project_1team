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
import {
  GoogleLogin,
  GoogleOAuthProvider,
  useGoogleLogin,
} from '@react-oauth/google';
import axiosUtils from '../../uitls/axiosUtils';
import authStore from '../../store/user/authStore';
import { useNavigate, useNavigation } from 'react-router-dom';
import { useAlert } from '../hook/useAlert';

const LoginForm = (props: any): JSX.Element => {
  const { userStore, authStore } = useStores();
  const [id, setId] = useState<String>('');
  const [password, setPassword] = useState<String>('');
  const navigate = useNavigate();
  const { openAlert } = useAlert();

  const requestAccessToken = async () => {
    axiosUtils
      .post('/auth/token/refresh')
      .then((refreshRes) => {
        return refreshRes.data.accessToken;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleEvent = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);
    if (data.get('id') == null || data.get('password') == null) {
      console.log(data.get('id'));
      console.log(data.get('password'));
    }
    await userStore
      .login(data)
      .then(async (res) => {
        if (200 <= res.status && res.status < 400) {
          localStorage.setItem('accessToken', res.data.accessToken);
          userStore.userInfo = {
            ...userStore.userInfo,
            username: res.data.username,
            accessToken: res.data.accessToken,
          };
        }
      })
      .catch((res) => {
        // return openAlert({ state: 'error', message: res.data.message });

        if (res.response.data.code === 'A001') {
          let accessToken = '';
          requestAccessToken().then((token) => {
            accessToken = token;
            console.log(accessToken);
            localStorage.setItem('accessToken', accessToken);
            userStore.userInfo = {
              ...userStore.userInfo,
              accessToken: accessToken,
            };
          });
        }
      });
    await authStore
      .findUserInfo()
      .then((res) => {
        navigate('/');
      })
      .catch((res) => {
        console.log(res);
        // return openAlert({ state: 'error', message: res.data.message });
      });
  };

  // const googleLogin = useGoogleLogin({
  //   scope: 'email profile',
  //   onSuccess: async () => {
  //     axiosUtils
  //       .oauth2Login('/login/oauth2/code/google')
  //       .then((res: any) => {
  //         console.log(res);
  //       });
  //   },
  //   onError: (res) => {
  //     console.error(res);
  //   },
  //   flow: 'auth-code',
  // });

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
          {/* <GoogleLogin size="large" ux_mode="popup" onSuccess={googleLogin} /> */}
          <GoogleOAuthProvider clientId="446534610656-14r63n2kho9aggjkp8ebi1rgods392uj.apps.googleusercontent.com">
            <GoogleLogin
              onSuccess={(res) => {
                console.log(res);
              }}
            />
          </GoogleOAuthProvider>

          <Button
            style={{ marginRight: '1rem' }}
            // 클릭 이벤트 수정해야 됨
            onClick={() =>
              (window.location.href =
                'http://localhost:8080/oauth2/authorization/google')
            }>
            구글 로그인 ( 백엔드 )
          </Button>
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
