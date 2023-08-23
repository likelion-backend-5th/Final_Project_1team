import Container from '@mui/material/Container';
import DropDown from '../atoms/DropDown.tsx';
import ArticleList from '../molecule/ArticleList.tsx';

const filterElements = [
  ['최신순', '1'],
  ['오래된순', '2'],
];

const Articles = () => {
  return (
    <Container>
      <h3>Articles</h3>
      <DropDown elements={filterElements} />
      <ArticleList />
    </Container>
  );
};

export default Articles;
