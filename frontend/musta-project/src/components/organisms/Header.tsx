import { Link } from 'react-router-dom';
import styled from 'styled-components';
import SearchInput from '../atoms/SearchInput';
import { useMemo, useState } from 'react';

const StyledHeader = styled.header`
  display: block;
  box-shadow: rgb(234, 234, 236) 0px 1px 0px;
  position: sticky;
  top: 0px;
  z-index: 100;
  width: 100%;
  background-color: rgba(255, 255, 255, 0.72);
`;

const StyledNav = styled.nav`
  margin: auto;
  padding: 0px 1rem;
  width: auto;
  max-width: 1280px;
  height: 52px;
  min-height: 3rem;
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: justify;
  justify-content: space-between;
  position: relative;
`;

const StyledNavLeft = styled.div`
  display: flex;
  flex-direction: row;
  -webkit-box-align: center;
  align-items: center;
  width: 100%;
  margin-right: 0.25rem;
`;

const StyledNavRight = styled.div`
  width: auto;
  display: block;
`;

const StyledMenuList = styled.div`
  display: flex;
  flex-direction: row;
  -webkit-box-align: center;
  align-items: center;
  position: relative;
`;

const Sign = styled.div`
  display: flex;
  flex-direction: row;
`;

const SearchBox = styled.div`
  width: 100%;
  display: block;
`;

const SearchContent = styled.div`
  display: flex;
  position: relative;
  flex-direction: column;
  margin-left: 1.5rem;
  max-width: 192px;
  transition: all 0.2s ease-out 0s;
`;

const MMenuList = styled.div``;

const Header = () => {
  const [width, setWidth] = useState<number>(window.innerWidth);

  const changeWidthSize = () => {
    setWidth(window.innerWidth);
  };

  useMemo(() => {
    window.addEventListener('resize', changeWidthSize);
    return () => {
      window.removeEventListener('resize', changeWidthSize);
    };
  }, []);

  return (
    <StyledHeader>
      <StyledNav>
        <StyledNavLeft>
          <Link to={'/'}>
            <img src="/img/templogo.png" height={23} />
          </Link>
          <SearchBox>
            <SearchContent>
              <SearchInput />
            </SearchContent>
          </SearchBox>
        </StyledNavLeft>
        <StyledNavRight>
          {width > 991 ? (
            <StyledMenuList>
              test
              <Sign />
            </StyledMenuList>
          ) : (
            <MMenuList>teststet</MMenuList>
          )}
        </StyledNavRight>
      </StyledNav>
    </StyledHeader>
  );
};

export default Header;
