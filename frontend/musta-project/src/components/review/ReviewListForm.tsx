import {
  List,
  Typography,
  Select,
  MenuItem,
  Pagination,
  SelectChangeEvent,
} from '@mui/material';
import { useParams } from 'react-router-dom';
import ReviewItemList from './ReviewItemList';
import { useEffect, useState } from 'react';
import { Review } from '../../types/review';
import axios from 'axios';

const ReviewListForm = () => {
  // URl에서 articleApiId 받음
  // 서버와 통신,
  // const params: any = useParams();
  // const articleApiId: any = params.articleApiId;
  const articleApiId: any = '8afe8b21-27a4-4dc4-ada6-f830d10a0c1f';
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);
  const [totalItems, setTotalItems] = useState(0);

  const [reviews, setReviews] = useState<Review[]>();

  useEffect(() => {
    fetchReviews(currentPage, itemsPerPage);
  }, [currentPage, itemsPerPage]);

  const fetchReviews = (page: number, limit: number) => {
    axios
      .get(`/api/article/${articleApiId}/review`, {
        params: {
          page,
          limit,
        },
      })
      .then((response) => {
        setReviews(response.data.content);
        setTotalItems(response.data.totalElements);
      })
      .catch((error) => {
        console.error('Error fetching reviews:', error);
      });
  };

  const [sortData, setSortData] = useState('descByDate'); // 초기값: 최신순

  let sortedReviews: Review[] = [];

  if (reviews && reviews.length > 0) {
    sortedReviews = reviews.slice().sort((a, b) => {
      if (sortData === 'descByDate') {
        const dateComparison =
          new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
        return -dateComparison;
      } else if (sortData === 'ascByDate') {
        const dateComparison =
          new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
        return dateComparison;
      } else if (sortData === 'descByPoint') {
        return b.point - a.point;
      } else {
        return a.point - b.point;
      }
    });
  }

  const handleSortDataChange = (event: any) => {
    const selectedSort = event.target.value;
    setSortData(selectedSort);
  };

  // 페이지 변경 핸들러
  const handlePageChange = (newPage: number) => {
    setCurrentPage(newPage);
  };

  // 한 페이지당 아이템 개수 변경 핸들러
  const handleItemsPerPageChange = (event: SelectChangeEvent<number>) => {
    const newItemsPerPage = event.target.value as number;
    setItemsPerPage(newItemsPerPage);
    fetchReviews(currentPage, newItemsPerPage);
  };

  return (
    <List>
      <Typography variant="h5" gutterBottom>
        리뷰
      </Typography>
      <List style={{ textAlign: 'left' }}>
        <Select size="small" value={sortData} onChange={handleSortDataChange}>
          <MenuItem value="descByDate">최신순</MenuItem>
          <MenuItem value="ascByDate">오래된 순</MenuItem>
          <MenuItem value="descByPoint">높은 별점 순</MenuItem>
          <MenuItem value="ascByPoint">낮은 별점 순</MenuItem>
        </Select>
        <Select
          size="small"
          value={itemsPerPage}
          onChange={handleItemsPerPageChange}>
          <MenuItem value={5}>5개씩</MenuItem>
          <MenuItem value={10}>10개씩</MenuItem>
          {/* 다른 원하는 아이템 개수도 추가할 수 있음 */}
        </Select>
      </List>
      <List>
        {sortedReviews.map((review, index) => (
          <ReviewItemList key={index} review={review} />
        ))}
      </List>
      <Pagination
        count={Math.ceil(totalItems / itemsPerPage)}
        page={currentPage}
        onChange={(event, newPage) => handlePageChange(newPage)}
      />
    </List>
  );
};

export default ReviewListForm;
