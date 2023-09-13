import {
  Avatar,
  Box,
  Button,
  Grid,
  Link,
  TextField,
  Typography,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { observer } from 'mobx-react-lite';
import useStores from '../../store/useStores';
import userStore from '../../store/user/userStore';
import AddressPost from '../atoms/AddressPost';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { AxiosResponse } from 'axios';
import { useAlert } from '../hook/useAlert';

const SignUpForm = () => {
  const userStore: userStore = useStores().userStore;

  const [id, setId] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [checkPassword, setCheckPassword] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [nickname, setNickname] = useState<string>('');
  const [phone, setPhone] = useState<string>('');

  const emailRegex = new RegExp(
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  );
  const passwordRegex = new RegExp(/^[a-zA-Z0-9!@#$%^&*]{8,24}$/);
  const nickNameRegex = new RegExp(/^[가-힣a-zA-Z0-9]{3, 14}$/);
  const idRegex = new RegExp(/^[a-zA-Z0-9]{8,24}$/);
  const phoneRegex = new RegExp(/01[016789]-[^0][0-9]{2,3}-[0-9]{3,4}/);

  const navigate = useNavigate();
  const { openAlert } = useAlert();
  const handleEvent = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);
    data.append('zipcode', userStore.userAddress.zipcode);
    data.append('city', userStore.userAddress.city);
    await userStore
      .handleSignup(data)
      .then(() => {
        navigate('/signUp/success');
      })
      .catch((res: AxiosResponse) => {
        openAlert({ state: 'error', message: res.data.message });
      });
  };

  useEffect(() => {
    return () => {};
  }, []);

  return (
    <Box
      sx={{
        marginTop: 8,
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
        회원가입
      </Typography>
      <Box component="form" noValidate onSubmit={handleEvent} sx={{ mt: 3 }}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              autoComplete="id"
              error={!idRegex.test(id)}
              name="id"
              required
              fullWidth
              id="id"
              label="아이디"
              autoFocus
              onChange={(e) => {
                setId(e.target.value);
              }}
              value={id}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              error={!passwordRegex.test(password)}
              fullWidth
              name="password"
              label="비밀번호"
              type="password"
              id="password"
              autoComplete="password"
              onChange={(e) => {
                setPassword(e.target.value);
              }}
              value={password}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              error={password != checkPassword || checkPassword.length < 1}
              name="check_password"
              label="비밀번호 확인"
              type="password"
              id="check_password"
              autoComplete="check_password"
              onChange={(e) => {
                setCheckPassword(e.target.value);
              }}
              value={checkPassword}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              error={!nickNameRegex.test(nickname)}
              name="nickname"
              label="닉네임"
              id="nickname"
              autoComplete="nickname"
              onChange={(e) => {
                setNickname(e.target.value);
              }}
              value={nickname}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              error={!emailRegex.test(email as string)}
              fullWidth
              id="email"
              label="이메일"
              helperText=""
              name="email"
              autoComplete="email"
              onChange={(e) => {
                setEmail(e.target.value);
              }}
              value={email}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              error={!phoneRegex.test(phone as string)}
              name="phoneNumber"
              label="핸드폰"
              id="phoneNumber"
              autoComplete="phoneNumber"
              onChange={(e) => {
                setPhone(e.target.value);
              }}
              value={phone}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <AddressPost />
          </Grid>
          <Grid item xs={12} sm={8}>
            <TextField
              fullWidth
              disabled
              name="zipcode"
              label="우편번호"
              id="zipcode"
              autoComplete="zipcode"
              value={userStore.userAddress.zipcode}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              disabled
              name="city"
              label="도로명 주소"
              id="city"
              autoComplete="city"
              value={userStore.userAddress.city}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              name="street"
              label="상세 주소"
              disabled={userStore.isDisabled()}
              id="street"
              autoComplete="street"
              onChange={(e) => userStore.setStreet(e)}
              value={userStore.userAddress.street}
            />
          </Grid>
          <Grid item xs={12}>
            <Button type="submit" fullWidth variant="contained">
              회원가입
            </Button>
          </Grid>
          <Grid item xs={12}>
            <Link href="/" style={{ textDecoration: 'none' }}>
              홈
            </Link>
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
};

export default observer(SignUpForm);
