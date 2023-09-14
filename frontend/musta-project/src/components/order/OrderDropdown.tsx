import React from 'react';
import { Select, MenuItem, SelectChangeEvent, styled } from '@mui/material';

const StyledSelect = styled(Select)(({ }) => ({
    minWidth: '150px', // 선택 상자의 최소 너비
    borderRadius: '5px', // 선택 상자 테두리 모양
    '& .MuiSelect-select': {
        padding: '8px', // 선택 상자 내부 패딩
    },
}));

interface DropdownProps {
    value: any;
    onChange: (event: any) => void;
    options: { value: string | number; label: string }[];
}

const Dropdown: React.FC<DropdownProps> = ({ value, onChange, options }) => {
    return (
        <StyledSelect value={value} onChange={onChange}>
            {options.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                    {option.label}
                </MenuItem>
            ))}
        </StyledSelect>
    );
};

export default Dropdown;
