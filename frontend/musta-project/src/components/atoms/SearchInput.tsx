import InputAdornment from '@mui/material/InputAdornment';
import TextField from '@mui/material/TextField';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import { useState } from 'react';
import { FormControl } from '@mui/material';

const SearchInput = () => {
  const [value, setValue] = useState<string>('');
  const [showClearIcon, setShowClearIcon] = useState<string>('none');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    e.defaultPrevented;
    setValue(e.target.value);
    setShowClearIcon(e.target.value.length < 1 ? 'none' : 'flex');
  };

  const onCLickVisibleEndButton = (): void => {
    setValue('');
    setShowClearIcon('none');
  };

  return (
    <FormControl margin="none" style={{ height: 32 }}>
      <TextField
        size="small"
        variant="outlined"
        placeholder="검색"
        onChange={handleChange}
        value={value}
        InputProps={{
          style: {
            borderRadius: '15px',
            height: 32,
          },
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment
              style={{ display: showClearIcon }}
              position="end"
              onClick={onCLickVisibleEndButton}>
              <ClearIcon />
            </InputAdornment>
          ),
        }}
      />
    </FormControl>
  );
};

export default SearchInput;
