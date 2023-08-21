import {styled } from "@mui/material";


const StyledDiv = styled('div')`
  display: flex;
  justify-content: center;
  align-items: center;
  width: auto;
  height: auto;
  margin: auto;
  padding: 8px 16px;
  max-width: 1280px;
  column-gap: 64px;
`;

export const HomePage = () => {
  return (
    <StyledDiv>
      <h3>home</h3>
    </StyledDiv>
  );
};
