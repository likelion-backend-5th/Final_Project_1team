import styled from 'styled-components';

const StyledFooter = styled.footer`
  color: black;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.72);
  position: sticky;
  bottom: 0px;
  z-index: 100;
  width: 100%;
`;

const FooterContent = styled.h3``;

const Footer = () => {
  return (
    <StyledFooter>
      <FooterContent>where footer</FooterContent>
    </StyledFooter>
  );
};

export default Footer;
