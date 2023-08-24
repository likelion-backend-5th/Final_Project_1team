import { SetStateAction, useEffect, useState } from 'react';
import { Article } from '../../types/article.ts';
import axios from 'axios';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import { AlbumCard } from '../molecule/AlbumCard.tsx';
import { Pagination, Skeleton } from '@mui/material';
import { GridPaginationModel } from '@mui/x-data-grid';

const PAGE_SIZE: number = 12; // 4x3 열을 위해 12로 변경
const baseURL = `http://localhost:8080/api/articles`;

export const ArticleList = () => {
  const [articleArrayList, setArticleArrayList] = useState<Article[]>([]);
  const [loading, setLoading] = useState(true);
  const [url, setUrl] = useState(`${baseURL}?size=${PAGE_SIZE}`);
  const [curPageNumber, setCurPageNumber] = useState(1);
  const [totalPageNumber, setTotalPageNumber] = useState(10);

  const fetchData = async () => {
    const response = axios.get(url);

    response.then((data) => {
      setArticleArrayList(data.data.content);
      setCurPageNumber(data.data.number + 1);
      setTotalPageNumber(data.data.totalPages);
      setTimeout(() => setLoading(false), 2000);
    });
  };

  useEffect(() => {
    setLoading(true);
    fetchData();
  }, [url]);

  const createAlbumRows = () => {
    const rows = [];
    for (let i = 0; i < articleArrayList.length; i += 4) {
      // 4개의 열로 변경
      const row = [];
      for (let j = 0; j < 4; j++) {
        // 4개의 열로 변경
        const index = i + j;
        if (index < articleArrayList.length) {
          row.push(
            <AlbumCard
              key={index}
              article={articleArrayList[index]}
              style={{ margin: '10px', width: '100%' }} // 간격과 카드 크기 조정
            />
          );
        } else {
          row.push(<div key={index}></div>); // Add empty placeholder for alignment
        }
      }
      rows.push(
        <Box
          key={i}
          display="flex"
          justifyContent="center"
          marginBottom="20px" // 행 간격을 더 띄움
        >
          {row}
        </Box>
      );
    }
    return rows;
  };

  const onPageChange = (_event: any, value: SetStateAction<number>) => {
    setCurPageNumber(value);
    const curUrl = new URL(url);
    const curUrlSearchParams = new URLSearchParams(curUrl.search);

    curUrlSearchParams.set('page', String(Number(value) - 1));

    setUrl(`${baseURL}?${curUrlSearchParams}`);
  };

  return (
    <>
      <Box display="flex" justifyContent="center">
        {loading ? (
          <Skeleton variant="rounded" width={1100} height={550} />
        ) : (
          <Box>{createAlbumRows()}</Box>
        )}
      </Box>
      <Box display="flex" justifyContent="center" marginTop="20px">
        <Pagination
          color="primary"
          size="medium"
          count={totalPageNumber}
          siblingCount={3}
          page={curPageNumber}
          defaultPage={1}
          showFirstButton
          showLastButton
          onChange={onPageChange}
        />
      </Box>
    </>
  );
};

export default ArticleList;
