import { Rating, styled } from '@mui/material';
import { TextField, Button, Container, Typography, Box } from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';
import EditIcon from '@mui/icons-material/Edit';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ContentsDiv = styled('div')`
  align: left;
`;

const ReviewEditItem = ({ review }: any) => {
  const [editReview, setEditReview] = useState(review);
  const navigate = useNavigate();

  const handleReviewChange = (event: any) => {
    setEditReview((prevData: any) => ({
      ...prevData,
      content: event.target.value,
    }));
  };

  const handleRatingChange = (newValue: any) => {
    setEditReview((prevData: any) => ({
      ...prevData,
      point: newValue,
    }));
  };

  const handleSaveReview = () => {
    // 리뷰 저장 로직 구현
    console.log('수정된 리뷰: ', editReview.content);
    console.log('수정된 별점: ', editReview.point);
  };

  const handleCancel = () => {
    navigate(-1);
  };

  return (
    <>
      <Typography align="left" variant="h5" gutterBottom>
        <div>
          <h3 style={{ margin: 'auto' }}>{review.username}님의 리뷰 수정</h3>
        </div>
        <ContentsDiv>
          <Container maxWidth="md">
            <Box mt={4}>
              <h4 style={{ borderBottom: '1px solid black' }}>리뷰 수정</h4>
              <TextField
                multiline
                rows={4}
                fullWidth
                variant="outlined"
                label="리뷰 내용"
                value={editReview.content}
                onChange={handleReviewChange}
                sx={{ marginTop: 2 }}
              />
              <h4 style={{ borderBottom: '1px solid black' }}>별점 수정</h4>
              <Rating
                name="simple-controlled"
                value={editReview.point}
                sx={{ marginTop: 2 }}
                onChange={(_event, newValue) => handleRatingChange(newValue)}
              />
              <div style={{ textAlign: 'right' }}>
                <Button
                  style={{ margin: '0.5rem' }}
                  variant="outlined"
                  color="warning"
                  startIcon={<EditIcon />}
                  onClick={() => handleSaveReview()}>
                  수정
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

export default ReviewEditItem;
