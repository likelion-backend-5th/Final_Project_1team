import { Box, Grid, Typography } from '@mui/material';
import { observer } from 'mobx-react-lite';
import { useEffect } from 'react';
import { Route, Routes, useNavigation } from 'react-router';
import { Link } from 'react-router-dom';

type menuLink = {
  to: string;
  name: string;
};

const UserPage = (props: any) => {
  // const navigation = useNavigation();

  const userLink: menuLink[] = [
    { to: '/user/profile', name: '사용자 프로필' },
    { to: '/user/password/edit', name: '비밀번호 수정' },
    { to: '/user/profile', name: '' },
    { to: '/user/profile', name: '' },
  ];

  useEffect(() => {
    // navigation.location?.pathname;
  }, []);

  return (
    <Grid container>
      <Grid xs={3}>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'left',
            justifyItems: 'left',
          }}>
          <p>사용자 관리</p>
          {userLink.map((userlink) => {
            return (
              <Link to={userlink.to}>
                <Typography
                  sx={{
                    my: 2,
                    mx: 3,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'left',
                  }}>
                  {userlink.name}
                </Typography>
              </Link>
            );
          })}
        </Box>
      </Grid>
      <Grid xs={9}>{props.children}</Grid>
    </Grid>
  );
};

export default observer(UserPage);
