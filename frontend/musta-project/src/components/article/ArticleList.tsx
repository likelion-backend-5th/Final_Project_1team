import {
  forwardRef,
  Ref,
  SetStateAction,
  useEffect,
  useImperativeHandle,
  useState,
} from 'react';
import { ArticleImpl, ofArticleImpl } from '../../types/article.ts';
import axios from 'axios';
import Box from '@mui/material/Box';
import { AlbumCard } from './AlbumCard.tsx';
import { Pagination, Skeleton } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { loadingTime } from '../../util/loadingUtil.ts';
import { getUrlSearchParams, setQuery } from '../../util/urlUtil.ts';

const PAGE_SIZE: number = 12; // 4x3 열을 위해 12로 변경
// const baseURL = `http://localhost:8080/api/articles`;
const baseURL = import.meta.env.VITE_API + 'api/articles';

export const ArticleList = forwardRef((props, ref) => {
  const [articleArrayList, setArticleArrayList] = useState<ArticleImpl[]>([]);
  const [loading, setLoading] = useState(true);
  const [url, setUrl] = useState(`${baseURL}?size=${PAGE_SIZE}`);
  const [curPageNumber, setCurPageNumber] = useState(1);
  const [totalPageNumber, setTotalPageNumber] = useState(10);
  const navigate = useNavigate();

  useImperativeHandle(ref, () => ({
    submitSearchParam(title: string, order: string) {
      let holderUrl = url;

      holderUrl = `${baseURL}?${setQuery(
        getUrlSearchParams(holderUrl),
        'title',
        title
      )}`;

      holderUrl = `${baseURL}?${setQuery(
        getUrlSearchParams(holderUrl),
        'order',
        order
      )}`;

      console.log(`${title}-${order}`);
      console.log(holderUrl);
      setUrl(holderUrl);
    },
  }));

  const fetchData = async () => {
    const response = axios.get(url);

    response.then((data) => {
      const group: ArticleImpl[] = [];

      data.data.content.forEach((item) => {
        group.push(ofArticleImpl(item));
      });

      setArticleArrayList(group);
      setCurPageNumber(data.data.number + 1);
      setTotalPageNumber(data.data.totalPages);
      //  TODO DEBUG용
      setTimeout(() => setLoading(false), loadingTime);
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
              key={articleArrayList[index].apiId}
              article={articleArrayList[index]}
              style={{ margin: '10px', width: '250px' }} // 간격과 카드 크기 조정
              detail={articleArrayList[index].apiId}
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
    window.scrollTo(0, 0);

    setUrl(
      `${baseURL}?${setQuery(
        getUrlSearchParams(url),
        'page',
        String(Number(value) - 1)
      )}`
    );
  };

  return (
    <>
      <Box display="flex" justifyContent="center">
        {loading ? (
          <Skeleton variant="rounded" width={1080} height={550} />
        ) : (
          <Box>{createAlbumRows()}</Box>
        )}
      </Box>
      <Box display="flex" justifyContent="center" marginY="20px">
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
});

export default ArticleList;
