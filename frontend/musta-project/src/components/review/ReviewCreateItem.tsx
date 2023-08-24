import { Rating, styled } from '@mui/material';
import { TextField, Button, Container, Typography, Box } from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';
import RateReviewIcon from '@mui/icons-material/RateReview';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ContentsDiv = styled('div')`
  align: left;
`;

const ReviewCreateItem = () => {
  const [createReview, setCreateReview] = useState({
    content: '',
    point: 0,
  });

  const navigate = useNavigate();

  const handleReviewChange = (event: any) => {
    setCreateReview((prevData) => ({
      ...prevData,
      content: event.target.value,
    }));
  };

  const handleRatingChange = (newPoint: any) => {
    setCreateReview((prevData: any) => ({
      ...prevData,
      point: newPoint,
    }));
  };

  const handleSaveReview = () => {
    // 리뷰 저장 로직 구현
    console.log('새로운 리뷰: ', createReview.content);
    console.log('별점: ', createReview.point);
    // 리뷰 저장 후 어떤 동작을 할지 구현하세요
    // navigate(`/review/${createReview.apiId}`); // 저장 후 리뷰 페이지로 이동 예시
  };

  const handleCancel = () => {
    navigate(-1); // 뒤로 가기
  };

  return (
    <>
      <Typography align="left" variant="h5" gutterBottom>
        <div>
          <h3 style={{ margin: 'auto' }}>리뷰 등록</h3>
        </div>
        <ContentsDiv>
          <Container maxWidth="md">
            <Box mt={4}>
              <h4 style={{ borderBottom: '1px solid black' }}>리뷰 작성</h4>
              <TextField
                multiline
                rows={4}
                fullWidth
                variant="outlined"
                label="리뷰 내용"
                value={createReview.content}
                onChange={handleReviewChange}
                sx={{ marginTop: 2 }}
              />
              <h4 style={{ borderBottom: '1px solid black' }}>별점 선택</h4>
              <Rating
                name="simple-controlled"
                value={Number(createReview.point)}
                sx={{ marginTop: 2 }}
                onChange={(_event, newPoint) => handleRatingChange(newPoint)}
              />
              <h4 style={{ borderBottom: '1px solid black' }}>이미지 첨부</h4>
              <div>
                <input
                  type="file"
                  // accept="image/*"
                  // onChange={handleImageChange}
                  // sx={{ marginTop: 2 }}
                />
              </div>
              <div style={{ textAlign: 'right' }}>
                <Button
                  style={{ margin: '0.5rem' }}
                  variant="outlined"
                  color="success"
                  startIcon={<RateReviewIcon />}
                  onClick={handleSaveReview}>
                  리뷰 등록
                </Button>
                <Button
                  variant="outlined"
                  color="error"
                  startIcon={<CancelIcon />}
                  onClick={handleCancel}>
                  취소
                </Button>
              </div>
            </Box>
          </Container>
        </ContentsDiv>
      </Typography>
    </>
  );
};

export default ReviewCreateItem;
