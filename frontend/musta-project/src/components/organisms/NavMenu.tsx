import { MenuItem, Typography } from '@mui/material';
import React from 'react';
import { Link } from 'react-router-dom';

interface NavMenuProps {
    pages: string[][];
    handleCloseNavMenu: () => void;
}

const NavMenu: React.FC<NavMenuProps> = ({ pages, handleCloseNavMenu }) => {
    return (
        <div>
            {pages.map((page) => (
                <MenuItem key={page[0]} onClick={handleCloseNavMenu} style={menuItemStyle}>
                    <Link to={page[1]} style={linkStyle}>
                        <Typography variant="body1" textAlign="center" style={typographyStyle}>
                            {page[0]}
                        </Typography>
                    </Link>
                </MenuItem>
            ))}
        </div>
    );
};

const menuItemStyle: React.CSSProperties = {
    padding: '8px', // 메뉴 아이템 패딩
};

const linkStyle: React.CSSProperties = {
    textDecoration: 'none', // 링크 밑줄 제거
    color: 'inherit', // 기본 텍스트 색상 유지
};

const typographyStyle: React.CSSProperties = {
    width: '100%', // 텍스트 가로 폭 100%
};

export default NavMenu;
