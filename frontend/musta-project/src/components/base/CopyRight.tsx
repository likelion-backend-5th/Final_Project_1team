import { Link, Typography } from '@mui/material';

const Copyright = (props: any) => {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}>
      {'Copyright © '}
      <Link
        color="inherit"
        href="https://github.com/likelion-backend-5th/Final_Project_1team/tree/main">
        like lion
      </Link>
      {new Date().getFullYear()}
    </Typography>
  );
};

export default Copyright;
