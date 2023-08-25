import {
  Box,
  Button,
  IconButton,
  Rating,
  Typography,
  styled,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import ReportIcon from '@mui/icons-material/Report';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Review } from '../../types/review';

const ContentsDiv = styled('div')`
  display: flex;
`;

const ReviewItem = ({ reviewApiId }: any) => {
  const navigate = useNavigate();

  // 현재 로그인 되어있는 계정의 username 더미
  const [username, setUsername] = useState('ArticleControllerTestUser1');

  const [detailReview, setDetailReview] = useState<Review>();
  const token =
    'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBcnRpY2xlQ29udHJvbGxlclRlc3RVc2VyMSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvYXV0aC9sb2dpbiIsImF1dGhvcml0aWVzIjpbXX0.fkAwNZ-vvk99ZnsZI-C9pdgrQ3qMjLr1bqLjG8X7sg0';

  useEffect(() => {
    // 리뷰 조회를 위한 API 호출
    axios
      .get(`/api/review/${reviewApiId}`, {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      })
      .then((response) => {
        setDetailReview(response.data);
      })
      .catch((error) => {
        console.error('Error fetching review:', error);
      });
  }, [reviewApiId, token]);

  const userHandler = () => {
    if (detailReview) {
      if (detailReview.username == username) return true;
      return false;
    }
  };

  const editHandler = () => {
    if (detailReview) {
      navigate(`/review/edit/${detailReview.apiId}`, {
        state: {
          apiId: `${detailReview.apiId}`,
          content: `${detailReview.content}`,
          point: `${detailReview.point}`,
          username: `${detailReview.username}`,
          reviewStatus: `${detailReview.reviewStatus}`,
        },
      });
    }
  };

  const handleReportClick = () => {
    // 여기에 신고 기능 연결하시면 됩니다
    console.log('버튼이 클릭되었습니다.');
  };

  const deleteHandler = async () => {
    if (detailReview) {
      axios
        .delete(`/api/review/${detailReview.apiId}`, {
          headers: {
            Authorization: 'Bearer ' + token,
          },
        })
        .then(() => {
          console.log('리뷰가 삭제되었습니다.');
          alert('리뷰가 성공적으로 삭제되었습니다.');
          navigate(-1);
        })
        .catch((error) => {
          console.error('Error deleting review:', error);
        });
    }
  };

  return (
    <div>
      {detailReview ? (
        <>
          <Typography align="left" variant="h5" gutterBottom>
            <div>
              <h3 style={{ margin: 'auto' }}>
                {detailReview.username}님의 리뷰
              </h3>
            </div>
            <br></br>
            <ContentsDiv>
              <div style={{ marginRight: '2%', textAlign: 'center' }}>
                <img
                  style={{ maxWidth: '400px' }}
                  src="/img/favicon.png"
                  title="예시 이미지"
                  alt="no Image"></img>
                <Box display="flex" justifyContent="center" alignItems="center">
                  <Box flex="1">
                    <Rating
                      name="read-only"
                      value={detailReview.point}
                      readOnly
                    />
                  </Box>
                  {/* 신고 버튼 */}
                  <IconButton onClick={handleReportClick} color="error">
                    <ReportIcon />
                  </IconButton>
                </Box>
              </div>
              <div>
                <h5>{detailReview.content}</h5>
              </div>
            </ContentsDiv>
            <div style={{ textAlign: 'right' }}>
              {userHandler() && (
                <>
                  <Button
                    style={{ margin: '0.5rem' }}
                    variant="outlined"
                    color="warning"
                    startIcon={<EditIcon />}
                    onClick={editHandler}>
                    수정
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    startIcon={<DeleteIcon />}
                    onClick={deleteHandler}>
                    삭제
                  </Button>
                </>
              )}
            </div>
          </Typography>
        </>
      ) : (
        <p>존재하지 않는 리뷰</p>
      )}
    </div>
  );
};

export default ReviewItem;
