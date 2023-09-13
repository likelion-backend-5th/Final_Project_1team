import { Copyright } from '@mui/icons-material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import {
  Avatar,
  Box,
  Button,
  Grid,
  Icon,
  Link,
  TextField,
  Typography,
  styled
} from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import { observer } from 'mobx-react-lite';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useStores from '../../store/useStores';
import { useAlert } from '../hook/useAlert';

const LoginForm = (props: any): JSX.Element => {
  const { userStore, authStore } = useStores();
  const [id, setId] = useState<String>('');
  const [password, setPassword] = useState<String>('');
  const navigate = useNavigate();
  const { openAlert } = useAlert();

  const handleEvent = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);
    if (data.get('id') == null || data.get('password') == null) {
      console.log(data.get('id'));
      console.log(data.get('password'));
    }
    await userStore
      .login(data)
      .then(async (res: any) => {
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
        return openAlert({ state: 'error', message: res.data.message });
      });
    await authStore
      .findUserInfo()
      .then((res: any) => {
        navigate('/');
        openAlert({
          state: 'success',
          message: authStore.userInfo?.username + '님 반갑습니다.',
        });
      })
      .catch((res: any) => {
        console.log(res);
        openAlert({ state: 'error', message: 'error' });
      });
  };

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

  const GoogleButton = styled(Button)`
  width: 100%;
  max-width: 680px;
  margin: 20px auto;
  height: 50px;
  cursor: pointer;
  font-size: 14px;
  font-family: 'Montserrat', sans-serif;
  border-radius: 8px;
  border: none;
  line-height: 40px;
  background: #5b90f0;
  color: white;
  transition: 0.2s linear;

  &:hover {
    box-shadow: 0 0 0 rgba(91, 144, 240, 0);
  }
`;

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
          <GoogleButton
            onClick={() =>
            (window.location.href =
              'http://localhost:8080/oauth2/authorization/google')
            }
            startIcon={<GoogleIcon />}
          >
            구글 로그인
          </GoogleButton>
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
