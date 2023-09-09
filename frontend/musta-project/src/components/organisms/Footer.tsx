import Typography from '@mui/material/Typography';
import styled from 'styled-components';
import { Box, Container, Grid, Link } from '@mui/material';
import {
  Email,
  Facebook,
  GitHub,
  Google,
  Instagram,
  Twitter,
} from '@mui/icons-material';

// const StyledFooter = styled.footer`
//   color: black;
//   text-align: center;
//   background-color: rgba(255, 255, 255, 0.72);
//   position: sticky;
//   bottom: 0px;
//   z-index: 100;
//   width: 100%;
// `;
//
// const FooterContent = styled.h3``;

const Footer = () => {
  return (
    <Box
      component="footer"
      mt={1}
      sx={{
        backgroundColor: (theme) =>
          theme.palette.mode === 'light'
            ? theme.palette.grey[200]
            : theme.palette.grey[800],
        p: 2,
      }}>
      <Container maxWidth="lg">
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography variant="h6" color="text.primary" gutterBottom>
              소개
            </Typography>
            <Typography variant="body2" color="text.secondary">
              반려동물을 키우는 이용자를 위한 반려동물 케어 매칭 서비스 '구해조
              집사'입니다.
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography variant="h6" color="text.primary" gutterBottom>
              링크
            </Typography>
            <Box display={'flex'} justifyContent={'center'}>
              <Link
                href="https://github.com/likelion-backend-5th/Final_Project_1team"
                color="inherit"
                marginX={'5px'}>
                <GitHub fontSize={'large'} />
              </Link>
              {/*<Link*/}
              {/*  href=""*/}
              {/*  color="inherit"*/}
              {/*  marginX={'5px'}>*/}
              {/*  <Email fontSize={'large'} />*/}
              {/*</Link>*/}
            </Box>
          </Grid>
        </Grid>
        <Box mt={2}>
          <Typography variant="body2" color="text.secondary" align="center">
            {'Copyright ©'}
            <Link
              color="inherit"
              href="https://your-website.com/"
              marginX={'5px'}>
              웹 사이트
            </Link>
            {2023}
          </Typography>
          <Typography variant="body2" color="text.secondary" align="center">
            {'Docs 🗎'}
            <Link
              color="inherit"
              href="https://drive.google.com/drive/folders/1WOUnXiP3zGcQmTGe7S1hljMKyI95Xq5P"
              marginX={'5px'}>
              구글 문서
            </Link>
          </Typography>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;
