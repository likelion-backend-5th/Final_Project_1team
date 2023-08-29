import {
  CssBaseline,
  Grid,
  Paper,
  ThemeProvider,
  createTheme,
} from '@mui/material';
import { observer } from 'mobx-react-lite';
import CopyRight from '../components/base/CopyRight';
import { Route, Routes } from 'react-router-dom';
import LoginForm from '../components/auth/LoginForm';
import SignUpForm from '../components/auth/SignUpForm';

const defaultTheme = createTheme();

const MainPage = (props: any): JSX.Element => (
  <ThemeProvider theme={defaultTheme}>
    <Grid container component="main" sx={{ height: '100vh' }}>
      <CssBaseline />
      <Grid
        item
        xs={false}
        lg={8}
        md={7}
        sm={4}
        sx={{
          backgroundImage: 'url(https://source.unsplash.com/random?wallpapers)',
          backgroundRepeat: 'no-repeat',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
      <Grid
        item
        lg={4}
        md={5}
        xs={12}
        sm={8}
        component={Paper}
        elevation={6}
        square>
        <Routes>
          <Route path="" Component={LoginForm} />
          <Route path="/login" Component={LoginForm} />
          <Route path="/signup" Component={SignUpForm} />
        </Routes>
        <CopyRight />
      </Grid>
    </Grid>
  </ThemeProvider>
);

export default observer(MainPage);
