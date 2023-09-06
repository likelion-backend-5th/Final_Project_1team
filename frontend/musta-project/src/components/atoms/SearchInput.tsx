import ClearIcon from '@mui/icons-material/Clear';
import SearchIcon from '@mui/icons-material/Search';
import { FormControl, InputAdornment, TextField } from '@mui/material';
import React, { KeyboardEvent, useState } from 'react';

interface SearchInputProps {
  onSearch: (searchValue: any) => void;
}

const SearchInput: React.FC<SearchInputProps> = ({ onSearch }) => {
  const [value, setValue] = useState<string>('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputValue = e.target.value;
    setValue(inputValue);
  };

  const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSearchClick();
    }
  };

  const handleSearchClick = () => {
    onSearch(value);
  };

  const handleClearClick = () => {
    setValue(''); // 입력값 초기화
  };

  return (
    <FormControl margin="none" style={{ height: 32 }}>
      <TextField
        size="small"
        variant="outlined"
        placeholder="검색"
        onChange={handleChange}
        onKeyPress={handleKeyPress}
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
            <InputAdornment position="end">
              <ClearIcon
                style={{ cursor: 'pointer', display: value ? 'block' : 'none' }}
                onClick={handleClearClick} // ClearIcon 클릭 시 입력값 초기화
              />
            </InputAdornment>
          ),
        }}
      />
      {/* <Button variant="contained" onClick={handleSearchClick}>
        Search
      </Button> */}
    </FormControl>
  );
};

export default SearchInput;
