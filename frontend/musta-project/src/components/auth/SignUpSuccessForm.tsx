import { Button, Grid } from '@mui/material';
import { Box } from '@mui/system';
import { useNavigate } from 'react-router';

const SignUpSuccessForm = () => {
  const navigate = useNavigate();

  return (
    <Grid>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <p>회원가입이 완료되었습니다.</p>
        <Button
          onClick={() => {
            navigate('/login');
          }}>
          로그인 하기
        </Button>
      </Box>
    </Grid>
  );
};

export default SignUpSuccessForm;
