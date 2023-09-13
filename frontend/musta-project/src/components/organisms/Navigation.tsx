import MenuIcon from '@mui/icons-material/Menu';
import AppBar from '@mui/material/AppBar';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import Toolbar from '@mui/material/Toolbar';
import Tooltip from '@mui/material/Tooltip';
import { observer } from 'mobx-react';
import * as React from 'react';
import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import useStores from '../../store/useStores';
import NavMenu from './NavMenu';
import { useAlert } from '../hook/useAlert';
import { Typography } from '@mui/material';
import userStore from '../../store/user/userStore';

const pages = [
  ['게시글', '/article'],
];

const baseSettings = [
  ['Profile', '/user/profile'],
  ['내가 주문한 목록', '/my/order/consume'],
  ['내가 판매한 목록', '/my/order/sell'],
  ['나의 채팅방', '/chatrooms'],
];
const Navigation = () => {
  const authStore = useStores().authStore;
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(
    null
  );
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(
    null
  );
  const navigate = useNavigate();
  const { openAlert } = useAlert();
  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  // 현재 사용자의 역할을 확인하고 그에 따른 settings 배열을 생성
  const settings = authStore.userInfo && authStore.userInfo.role.includes('ROLE_ADMIN')
      ? [...baseSettings, ['신고 목록', '/reports']]
      : baseSettings;

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handlePageClick = (path: string) => {
    handleCloseNavMenu();
    navigate(path); // 해당 경로로 페이지 이동
  };

  const handleLogout = () => {
    authStore.logout();
    navigate('/');
  };

  useEffect(() => {
    if (localStorage.getItem('accessToken') == null) {
      return;
    }
    if (!authStore.userInfo) {
      try {
        authStore
          .findUserInfo()
          .then((res: any) => {
            authStore.userInfo = res.data;
            navigate('/');
          })
      } catch (error) {
        localStorage.remove('accessToken');
      }
    }
    return () => {};
  }, []);

  return (
    <AppBar position="static" color="inherit" elevation={0}>
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
            <Link to={'/'}>
              <img src="/img/templogo.png" height={23} />
            </Link>
          </Box>
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}>
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
              color="black">
              <NavMenu pages={pages} handleCloseNavMenu={handleCloseNavMenu} />
            </Menu>
          </Box>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            {pages.map((page) => (
              <Button
                key={page[0]}
                onClick={() => handlePageClick(page[1])}
                sx={{ my: 2, color: 'black', display: 'block' }}>
                {page[0]}
              </Button>
            ))}
          </Box>
          {(authStore.userInfo === undefined && (
            <Box>
              <Link to={'/login'} style={{ textDecoration: 'none' }}>
                login
              </Link>
            </Box>
          )) || (
            <Box>
              <Tooltip title="">
                <IconButton
                  onClick={handleOpenUserMenu}
                  disableRipple
                  sx={{ borderRadius: '5px', outline: 0 }}>
                  <Avatar
                    alt="Remy Sharp"
                    src={authStore.userInfo?.image_url}
                    sx={{ marginX: '10px' }}
                  />
                  <Typography>
                    {/*// TODO 임시로 넣은 코드*/}
                    {/*{authStore.userInfo?.username}*/}
                    {!authStore.userInfo?.username
                      ? 'User123'
                      : authStore.userInfo?.username}
                  </Typography>
                </IconButton>
              </Tooltip>
              <Menu
                sx={{ mt: '45px' }}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                autoFocus={false}
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}>
                <NavMenu
                  pages={settings}
                  handleCloseNavMenu={handleCloseUserMenu}
                />
              </Menu>
              <button onClick={handleLogout}>로그아웃</button>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default observer(Navigation);
