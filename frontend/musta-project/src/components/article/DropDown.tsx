import { alpha, MenuProps, styled } from '@mui/material';
import Menu from '@mui/material/Menu';
import React, { useEffect } from 'react';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import { KeyboardArrowDown } from '@mui/icons-material';

const StyledMenu = styled((props: MenuProps) => (
  <Menu
    elevation={0}
    anchorOrigin={{
      vertical: 'bottom',
      horizontal: 'right',
    }}
    transformOrigin={{
      vertical: 'top',
      horizontal: 'right',
    }}
    {...props}
  />
))(({ theme }) => ({
  '& .MuiPaper-root': {
    borderRadius: 6,
    marginTop: theme.spacing(1),
    minWidth: 180,
    color:
      theme.palette.mode === 'light'
        ? 'rgb(55, 65, 81)'
        : theme.palette.grey[300],
    boxShadow:
      'rgb(255, 255, 255) 0px 0px 0px 0px, rgba(0, 0, 0, 0.05) 0px 0px 0px 1px, rgba(0, 0, 0, 0.1) 0px 10px 15px -3px, rgba(0, 0, 0, 0.05) 0px 4px 6px -2px',
    '& .MuiMenu-list': {
      padding: '4px 0',
    },
    '& .MuiMenuItem-root': {
      '& .MuiSvgIcon-root': {
        fontSize: 18,
        color: theme.palette.text.secondary,
        marginRight: theme.spacing(1.5),
      },
      '&:active': {
        backgroundColor: alpha(
          theme.palette.primary.main,
          theme.palette.action.selectedOpacity
        ),
      },
    },
  },
}));

interface DropDownProps {
  elements: string[][];
  setValue;
}

export default function DropDown(props: DropDownProps) {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [innerValue, setInnerValue] = React.useState<string>(
    props.elements[0][2]
  );
  const [buttonText, setButtonText] = React.useState<string>(
    props.elements[0][0]
  );
  const open = Boolean(anchorEl);

  useEffect(() => {}, [innerValue]);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const returnChangedValueToParent = (item: string) => {
    props.setValue(item);
  };

  const elementComp = props.elements.map(function (item) {
    return (
      <MenuItem
        key={item[1]}
        onClick={() => {
          handleClose();
          setInnerValue(item[2]);
          returnChangedValueToParent(item[2]);
          setButtonText(item[0]);
        }}
        disableRipple>
        {item[0]}
      </MenuItem>
    );
  });

  return (
    <>
      <Button
        id="demo-customized-button"
        aria-controls={open ? 'demo-customized-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        variant="contained"
        disableElevation
        defaultValue={props.elements[0][0]}
        onClick={handleClick}
        endIcon={<KeyboardArrowDown />}>
        {buttonText}
      </Button>
      <StyledMenu
        id="demo-customized-menu"
        MenuListProps={{
          'aria-labelledby': 'demo-customized-button',
        }}
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}>
        {elementComp}
      </StyledMenu>
    </>
  );
}
