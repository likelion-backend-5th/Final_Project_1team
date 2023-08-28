import Container from '@mui/material/Container';
import ArticleList from './ArticleList.tsx';
import Box from '@mui/material/Box';
import SearchInputMui from './SearchInputMui.tsx';
import Button from '@mui/material/Button';
import { AddBox } from '@mui/icons-material';

const Articles = () => {
  return (
    <Container>
      <h3>Articles</h3>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          marginX: '10px',
        }}>
        <Button variant="contained" startIcon={<AddBox />} href="/article/post">
          글쓰기
        </Button>
        <SearchInputMui />
      </Box>
      <ArticleList />
    </Container>
  );
};

export default Articles;
