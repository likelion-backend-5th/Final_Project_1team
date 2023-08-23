import Container from '@mui/material/Container';
import DropDown from '../atoms/DropDown.tsx';

const filterElements = [
  ['날짜 내림차순', '1'],
  ['날짜 오름차순', '2'],
];

const Articles = () => {
  return (
    <Container>
      <h3>Articles</h3>
      <DropDown elements={filterElements} />
    </Container>
  );
};

export default Articles;
