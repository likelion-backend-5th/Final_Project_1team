import { observer } from 'mobx-react-lite';
import useStore from '../../store/useStores';
import userStore from '../../store/user/userStore';
import { Box, Button, Grid, TextField, Typography } from '@mui/material';

const ProfileForm = () => {
  const userStore: userStore = useStore().userStore;

  return (
    <Grid sx={{ mt: 10, mx: 10 }} xs={9} height={'80vh'}>
      {/* <Typography fontSize={28}>프로필</Typography>
      <TextField
        sx={{ mt: 3, mb: 2 }}
        required
        fullWidth
        error={!passwordRegex.test(password)}
        id="password"
        label="비밀번호"
        name="password"
        type="password"
        autoComplete="password"
        autoFocus
        onChange={(e) => {
          setPassword(e.target.value);
        }}
        value={password}
      />
      <Box component="form" onSubmit={handleEvent} margin="0 auto">
        <TextField
          sx={{ mt: 3, mb: 2 }}
          required
          fullWidth
          error={!passwordRegex.test(newPassword)}
          id="newPassword"
          label="새 비밀번호"
          name="newPassword"
          type="password"
          autoComplete="newPassword"
          autoFocus
          onChange={(e) => {
            setNewPassword(e.target.value);
          }}
          value={newPassword}
        />
        <TextField
          sx={{ mt: 3, mb: 2 }}
          required
          fullWidth
          error={!passwordRegex.test(newPasswordCheck)}
          name="newPasswordCheck"
          label="새 비밀번호 확인"
          type="password"
          id="newPasswordCheck"
          autoComplete="newPasswordCheck"
          onChange={(e) => {
            setNewPasswordCheck(e.target.value);
          }}
          value={newPasswordCheck}
        />
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}>
          비밀번호 변경
        </Button>
      </Box> */}
    </Grid>
  );
};

export default observer(ProfileForm);
