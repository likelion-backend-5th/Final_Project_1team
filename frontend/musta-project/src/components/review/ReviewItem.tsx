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
import useStores from '../../store/useStores';
import { Review } from '../../types/review';
import { deleteReview, getDetailReview } from '../../store/auth-action';
import { Carousel } from 'react-responsive-carousel';

const ContentsDiv = styled('div')`
  display: flex;
`;

function getReviewApiId() {
  const pathnames = location.pathname.split('/');
  return `${pathnames.pop()}`;
}

const ReviewItem = ({ reviewApiId }: any) => {
  const navigate = useNavigate();
  const authStore = useStores().authStore;

  const [detailReview, setDetailReview] = useState<Review>();

  useEffect(() => {
    // 리뷰 조회를 위한 API 호출
    getDetailReview(reviewApiId)
      .then((response: any) => {
        setDetailReview(response.data);
      })
      .catch((error: any) => {
        console.error('Error fetching review:', error);
        alert('존재하지 않는 리뷰 입니다.');
        navigate(-1);
      });
  }, [reviewApiId]);

  const userHandler = () => {
    if (detailReview) {
      if (detailReview.username == authStore.userInfo?.username) return true;
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
          images: `${detailReview.images}`,
        },
      });
    }
  };

  const handleReportClick = () => {
    console.log('버튼이 클릭되었습니다.');
    const type = 'review';
    const id = getReviewApiId();
    navigate(`/report/${type}/${id}`);
  };

  const deleteHandler = async () => {
    if (detailReview) {
      deleteReview(reviewApiId)
        .then(() => {
          console.log('리뷰가 삭제되었습니다.');
          alert('리뷰가 성공적으로 삭제되었습니다.');
          navigate(-1);
        })
        .catch((error: any) => {
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
                {detailReview.images.length == 0 ? (
                  <img
                    style={{ maxWidth: '400px' }}
                    src="/img/favicon.png"
                    title="예시 이미지"
                    alt="no Image"></img>
                ) : (
                  <Box style={{ maxWidth: '400px', textAlign: 'center' }}>
                    <Carousel
                      showArrows={true}
                      infiniteLoop={true}
                      selectedItem={0}>
                      {detailReview.images.map((preview, index) => (
                        <div
                          key={index}
                          style={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            maxWidth: '400px',
                          }}>
                          <img
                            src={preview.fullPath}
                            alt={`Image ${index}`}
                            style={{
                              maxWidth: '100%',
                              height: '100%',
                              objectFit: 'contain',
                            }}
                          />
                        </div>
                      ))}
                    </Carousel>
                  </Box>
                )}
                <Box display="flex" justifyContent="center" alignItems="center">
                  <Box flex="1" style={{ marginLeft: '12%' }}>
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
                    리뷰 수정
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    startIcon={<DeleteIcon />}
                    onClick={deleteHandler}>
                    리뷰 삭제
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
