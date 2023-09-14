import { observer } from 'mobx-react-lite';
import useStore from '../../store/useStores';
import userStore from '../../store/user/userStore';
import {
  Avatar,
  Box,
  Button,
  FormControl,
  Grid,
  Input,
  TextField,
  Typography,
} from '@mui/material';
import { styled } from 'styled-components';
import { useEffect, useLayoutEffect, useRef, useState } from 'react';
import { URL } from 'url';
import authStore from '../../store/user/authStore';
import CustomAlert from '../base/CustomAlert';
import { useAlert } from '../hook/useAlert';
import { s3Client, uploadImagesToS3 } from '../../util/s3Client';
import AddressPost from '../atoms/AddressPost';

const CustomBox = styled(Box)`
  border: 1px solid #efefef;
  padding: 2rem;
  display: 'flex';
  justify-content: 'center';
  align-items: 'center';
  margin-bottom: 20px;
  border-radius: 10px;
  margin-right: auto;
`;

const ContentBox = styled(Box)`
  width: '100%';
  margin-right: 10px;
  margin-bottom: 10px;
`;

const ButtonBox = styled(Box)`
  margin-bottom: '10px';
`;

const ProfileForm = () => {
  const [image, setImage] = useState<{ preview: string; raw: any }>({
    preview: '',
    raw: {},
  });
  const [email, setEmail] = useState<string>('');
  const [nickname, setNickname] = useState<string>('');
  const imageRef = useRef<HTMLInputElement | null>(null);
  const { userStore, authStore } = useStore();
  const { openAlert } = useAlert();

  const emailRegex = new RegExp(
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  );

  const handleProfile = async (data: any) => {
    const res = await uploadImagesToS3(image.raw, 'user').catch((res) =>
      openAlert({ state: 'error', message: 'error' })
    );
    console.log(res);
    if (res && res.length > 0) {
      await userStore
        .changeImage(res[0].s3URL + res[0].filename)
        .then((res: any) => {})
        .catch((res: any) => openAlert({ state: 'error', message: 'error' }));

      await authStore
        .findUserInfo()
        .catch((res) => openAlert({ state: 'error', message: 'info error' }));
    }
  };

  const handleNicknameChange = async (e: any) => {
    e.preventDefault();

    if (!emailRegex.test(email)) {
      return;
    }

    await userStore
      .changeEmail({ email: email })
      .then((res: any) => {
        openAlert({ state: 'success', message: '이메일이 변경되었습니다.' });
      })
      .catch((res) =>
        openAlert({ state: 'error', message: 'fail email change' })
      );
    setEmail('');

    await authStore
      .findUserInfo()
      .catch((res) => openAlert({ state: 'error', message: 'info error' }));
  };
  const handleEmailChange = async (e: any) => {
    e.preventDefault();

    if (!emailRegex.test(email)) {
      return;
    }

    await userStore
      .changeEmail({ email: email })
      .then((res: any) => {
        openAlert({ state: 'success', message: '이메일이 변경되었습니다.' });
      })
      .catch((res) =>
        openAlert({ state: 'error', message: 'fail email change' })
      );
    setEmail('');

    await authStore
      .findUserInfo()
      .catch((res) => openAlert({ state: 'error', message: 'info error' }));
  };

  const handleAddressChange = async (e: any) => {
    if (
      userStore.userAddress.city.length === 0 ||
      userStore.userAddress.zipcode.length === 0
    ) {
      openAlert({ state: 'error', message: '입력을 다시 확인해주세요.' });
      return;
    }

    await userStore
      .chnageAddress()
      .then(() => {
        openAlert({ state: 'success', message: '주소가 변경되었습니다.' });
      })
      .catch(() => {
        openAlert({ state: 'error', message: '주소 변경 실패' });
      });
  };

  const handleFileChange = async (e: any) => {
    e.preventDefault();

    let reader = new FileReader();
    let file = e.target.files;
    reader.onloadend = () => {
      setImage({
        preview: reader.result?.toString() as any,
        raw: file,
      });
    };
    reader.readAsDataURL(e.target.files[0]);
  };

  const handleUpload = async (e: any) => {
    e.preventDefault();
    imageRef.current?.click();
  };

  useEffect(() => {
    setImage({
      preview: authStore.userInfo?.image_url
        ? authStore.userInfo.image_url
        : '',
      raw: {},
    });
    setEmail(authStore.userInfo?.email ? authStore.userInfo.email : '');
    setNickname(
      authStore.userInfo?.nickname ? authStore.userInfo.nickname : ''
    );
    console.log(authStore.userInfo);
  }, []);

  return (
    <Grid sx={{ mt: 10, mx: 10 }} xs={9} minHeight={'80vh'} display={'block'}>
      <CustomBox>
        <FormControl>
          <Box>
            <h2 style={{ display: 'block' }}>이미지 변경</h2>
          </Box>
          <input
            name={'image-upload'}
            ref={imageRef}
            type={'file'}
            accept="image/jpg, image/jpeg, image/png"
            id={'image-upload'}
            onChange={handleFileChange}
            hidden
          />
          <Box display={'flex'} width={'100%'}>
            <ContentBox>
              <Avatar
                sx={{ height: '110px', width: '110px' }}
                src={image.preview}
              />
            </ContentBox>
            <ContentBox sx={{ display: 'flex', alignItems: 'center' }}>
              <Button
                onClick={handleUpload}
                color="success"
                variant="contained">
                수정
              </Button>
            </ContentBox>
            <ContentBox sx={{ display: 'flex', alignItems: 'center' }}>
              <Button
                onClick={() => {
                  setImage({ preview: '', raw: '' });
                }}
                color="inherit"
                variant="contained">
                취소
              </Button>
            </ContentBox>
          </Box>
          <Button
            type="submit"
            size="large"
            variant="contained"
            onClick={handleProfile}>
            저장하기
          </Button>
        </FormControl>
      </CustomBox>
      <CustomBox>
        <FormControl>
          <Box>
            <h2 style={{ display: 'block' }}>이메일 변경</h2>
          </Box>
          <Box display={'flex'} width={'100%'}>
            <ContentBox sx={{ display: 'flex', alignItems: 'center' }}>
              <TextField
                autoComplete="email"
                name="email"
                required
                fullWidth
                id="email"
                label="이메일"
                autoFocus
                onChange={(e) => {
                  setEmail(e.target.value);
                }}
                value={email}
              />
            </ContentBox>
          </Box>
          <Button
            type="submit"
            size="large"
            variant="contained"
            onClick={handleEmailChange}>
            저장하기
          </Button>
        </FormControl>
      </CustomBox>
      <CustomBox>
        <Box>
          <h2 style={{ display: 'block' }}>주소 변경</h2>
        </Box>
        <Box
          sx={{
            width: '100%',
            display: 'flex',
            alignItems: 'center',
            flexDirection: 'column',
          }}>
          <ButtonBox>
            <AddressPost />
          </ButtonBox>
          <ButtonBox>
            <TextField
              fullWidth
              disabled
              name="zipcode"
              label="우편번호"
              id="zipcode"
              autoComplete="zipcode"
              value={userStore.userAddress.zipcode}
            />
          </ButtonBox>
          <ButtonBox>
            <TextField
              fullWidth
              disabled
              name="city"
              label="도로명 주소"
              id="city"
              autoComplete="city"
              value={userStore.userAddress.city}
            />
          </ButtonBox>
          <ButtonBox>
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
          </ButtonBox>
        </Box>
        <Button
          type="submit"
          size="large"
          variant="contained"
          fullWidth
          onClick={handleAddressChange}>
          저장하기
        </Button>
      </CustomBox>
      {/* <CustomBox>
        <FormControl>
          <Box>
            <h2 style={{ display: 'block' }}>닉네임 변경</h2>
          </Box>
          <ButtonBox>
            <TextField
              fullWidth
              name="nickname"
              label="닉네임"
              id="nickname"
              autoComplete="nickname"
              onChange={(e) => setNickname(e.target.value)}
              value={userStore.userAddress.street}
            />
          </ButtonBox>
          <Button type="submit" size="large" variant="contained" onClick={handleNicknameChange}>
            저장하기
          </Button>
        </FormControl> 
      </CustomBox>*/}
    </Grid>
  );
};

export default observer(ProfileForm);
