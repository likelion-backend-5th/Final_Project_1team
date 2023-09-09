import Container from '@mui/material/Container';
import ArticleList from './ArticleList.tsx';
import Box from '@mui/material/Box';
import SearchInputMui from './SearchInputMui.tsx';
import Button from '@mui/material/Button';
import { AddBox } from '@mui/icons-material';
import { useEffect, useRef, useState } from 'react';

const Articles = () => {
  const [title, setTitle] = useState();
  //  asc, desc
  const [order, setOrder] = useState();
  const childRef = useRef(null);

  const onClickSubmit = () => {
    childRef.current.submitSearchParam(title, order);
  };

  return (
    <Container>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          marginX: '10px',
          background: '#ffffff',
          marginTop: '10px',
          padding: '10px',
          borderRadius: '15px',
          border: '1px solid #d0d0d0',
          // boxShadow: 4,
        }}>
        <Button variant="contained" startIcon={<AddBox />} href="/article/post">
          글쓰기
        </Button>
        <SearchInputMui
          title={title}
          onChangeTitle={setTitle}
          order={order}
          onChangeOrder={setOrder}
          onClickSubmit={onClickSubmit}
        />
      </Box>
      <ArticleList ref={childRef} />
    </Container>
  );
};

export default Articles;
