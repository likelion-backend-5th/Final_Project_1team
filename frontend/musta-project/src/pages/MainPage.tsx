import { CssBaseline, Grid, createTheme } from '@mui/material';
import { observer } from 'mobx-react-lite';

const defaultTheme = createTheme();

const MainPage = (props: any): JSX.Element => (
  <Grid container component="main" sx={{ height: '100vh' }}>
    <Grid
      item
      xs={false}
      lg={12}
      md={12}
      sm={12}
      sx={{
        backgroundImage: 'url(https://source.unsplash.com/random?wallpapers)',
        backgroundRepeat: 'no-repeat',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    />
  </Grid>
);

export default observer(MainPage);
