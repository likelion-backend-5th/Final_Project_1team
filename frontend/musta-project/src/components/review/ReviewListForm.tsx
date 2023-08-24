import { List, Typography, Select, MenuItem } from '@mui/material';
import { useParams } from 'react-router-dom';
import ReviewItemList from './ReviewItemList';
import { useState } from 'react';

const ReviewListForm = () => {
  // URl에서 articleApiId 받음
  // 서버와 통신,
  const params: any = useParams();
  let articleApiId: any = params.articleApiId;

  // 더미 데이터
  const reviews = [
    {
      apiId: 'review1',
      username: 'user1',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 4,
      createdAt: '2023-08-19',
      reviewStatus: 'UPLOAD',
    },
    {
      apiId: 'review2',
      username: 'user2',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 5,
      createdAt: '2023-08-18',
      reviewStatus: 'UPDATED',
    },
    {
      apiId: 'review3',
      username: 'user3',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 3,
      createdAt: '2023-08-20',
      reviewStatus: 'UPLOAD',
    },
    {
      apiId: 'review4',
      username: 'user4',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 5,
      createdAt: '2023-08-01',
      reviewStatus: 'UPDATED',
    },
    {
      apiId: 'review5',
      username: 'user5',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 2,
      createdAt: '2023-08-24',
      reviewStatus: 'UPLOAD',
    },
    {
      apiId: 'review6',
      username: 'user6',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 1,
      createdAt: '2023-08-21',
      reviewStatus: 'UPLOAD',
    },
  ];

  const [sortData, setSortData] = useState('descByDate'); // 초기값: 최신순

  const sortedReviews = reviews.slice().sort((a, b) => {
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

  const handleSortDataChange = (event: any) => {
    const selectedSort = event.target.value;
    setSortData(selectedSort);
  };
  //   const handle

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
      </List>
      <List>
        {sortedReviews.map((review, index) => (
          <ReviewItemList key={index} review={review} />
        ))}
      </List>
    </List>
  );
};

export default ReviewListForm;
