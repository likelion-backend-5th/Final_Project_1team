import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import DropDown from './DropDown.tsx';
import Box from '@mui/material/Box';

const filterElements = [
  ['최신순', '1', 'desc'],
  ['오래된순', '2', 'asc'],
];

export default function SearchInputMui() {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
      <DropDown elements={filterElements} />
      <Paper
        component="form"
        sx={{
          p: '2px 4px',
          display: 'flex',
          alignItems: 'center',
          width: 400,
          marginLeft: '10px', // 추가
        }}>
        <InputBase sx={{ ml: 1, flex: 1 }} placeholder="검색" />
        <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
        <IconButton type="button" aria-label="search">
          <SearchIcon />
        </IconButton>
      </Paper>
    </Box>
  );
}
