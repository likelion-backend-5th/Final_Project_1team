import Container from '@mui/material/Container';
import DropDown from '../atoms/DropDown.tsx';
import ArticleList from './ArticleList.tsx';
import Box from '@mui/material/Box';
import { Typography } from '@mui/material';
import SearchInput from '../atoms/SearchInput.tsx';
import SearchInputMui from '../atoms/SearchInputMui.tsx';

const filterElements = [
  ['최신순', '1'],
  ['오래된순', '2'],
];

const Articles = () => {
  return (
    <Container>
      <h3>Articles</h3>
      <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
        <DropDown elements={filterElements} />
        {/*<SearchInput />*/}
        <SearchInputMui />
      </Box>
      <ArticleList />
    </Container>
  );
};

export default Articles;
