
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

const OauthSignUpForm = () => {
  const userStore: userStore = useStores().userStore;
  const [phone, setPhone] = useState<string>('');
  const [street, setStreet] = useState<string>('');

  const phoneRegex = new RegExp(/01[016789]-[^0][0-9]{2,3}-[0-9]{3,4}/);

  const navigate = useNavigate();
  const { openAlert } = useAlert();
  const handleEvent = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    data.append('phoneNumber', phone);
    data.append('zipcode', userStore.userAddress.zipcode);
    data.append('city', userStore.userAddress.city);
    data.append('street', street);
    console.log(phone+" "+userStore.userAddress.zipcode+" "+userStore.userAddress.city+" "+street);

    await userStore
      .handleOAuthSignUp(data)
      .then(() => {
        navigate('/');
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
              onChange={(e) => setStreet(e.target.value)}
              value={street}
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

export default observer(OauthSignUpForm);
