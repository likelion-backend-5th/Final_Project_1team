import { Rating, styled } from '@mui/material';
import { TextField, Button, Container, Typography, Box } from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';
import RateReviewIcon from '@mui/icons-material/RateReview';
import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { createReview } from '../../store/auth-action';
import { uploadImagesToS3 } from '../../util/s3Client';
import { HideImage, InsertPhoto } from '@mui/icons-material';
import { Carousel } from 'react-responsive-carousel';

const ContentsDiv = styled('div')`
  align: left;
`;

const ReviewCreateItem = () => {
  const { articleApiId, orderApiId } = useParams();
  const [createReviewData, setCreateReviewData] = useState({
    content: '',
    point: 0,
  });
  const [imageFiles, setImageFiles] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);

  const navigate = useNavigate();

  const handleReviewChange = (event: any) => {
    setCreateReviewData((prevData) => ({
      ...prevData,
      content: event.target.value,
    }));
  };

  const handleRatingChange = (newPoint: any) => {
    setCreateReviewData((prevData: any) => ({
      ...prevData,
      point: newPoint,
    }));
  };

  const handleImageChange = (event: any) => {
    let newImageFiles: any = Array.from(event.target.files);

    if (newImageFiles.length > 5) {
      alert('이미지는 최대 5개까지 첨부 가능합니다.');
      newImageFiles = newImageFiles.slice(0, 5);
    }

    setImageFiles(newImageFiles.slice(0, 5));
    const newImagePreviews = newImageFiles.map((file: any) =>
      URL.createObjectURL(file)
    );
    setImagePreviews(newImagePreviews);
  };

  const onClickRemoveImages = () => {
    setImageFiles([]);
    setImagePreviews([]);
  };

  const handleSaveReview = async () => {
    try {
      uploadImagesToS3(imageFiles, 'review').then((result) => {
        if (result) {
          console.log(result);
          createReview(
            articleApiId,
            orderApiId,
            createReviewData.content,
            createReviewData.point,
            result
          )
            .then((response: any) => {
              console.log('리뷰 저장 완료:', response.data);
              // 저장 후 어떤 동작을 할지 구현
              navigate(`/review/${response.data.apiId}`); // 저장 후 리뷰 페이지로 이동 예시
            })
            .catch((error: any) => {
              console.error('Error saving review: ', error);
              // 에러 처리
            });
        }
      });
    } catch (error) {
      console.error('Error saving review: ', error);
    }
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
                value={createReviewData.content}
                onChange={handleReviewChange}
                sx={{ marginTop: 2 }}
              />
              <h4 style={{ borderBottom: '1px solid black' }}>별점 선택</h4>
              <Rating
                name="simple-controlled"
                value={Number(createReviewData.point)}
                sx={{ marginTop: 2 }}
                onChange={(_event, newPoint) => handleRatingChange(newPoint)}
              />
              <h4 style={{ borderBottom: '1px solid black' }}>이미지 첨부</h4>
              <Box style={{ textAlign: 'center' }}>
                <Carousel
                  showArrows={true}
                  infiniteLoop={true}
                  selectedItem={0}>
                  {imagePreviews.map((preview, index) => (
                    <div
                      key={index}
                      style={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        height: '300px',
                      }}>
                      <img
                        src={preview}
                        alt={`Image Preview ${index}`}
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
              <Box display="flex" justifyContent="space-between">
                <Box>
                  <input
                    accept="image/*"
                    id="image-file"
                    type="file"
                    multiple
                    onChange={handleImageChange}
                    style={{ display: 'none' }}
                  />
                  <label htmlFor="image-file">
                    <Button
                      variant="outlined"
                      color="primary"
                      component="span"
                      startIcon={<InsertPhoto />}
                      style={{ margin: '0.5rem' }}>
                      이미지 첨부
                    </Button>
                  </label>
                  {imagePreviews.length > 0 ? (
                    <Button
                      variant="outlined"
                      color="error"
                      component="span"
                      startIcon={<HideImage />}
                      onClick={onClickRemoveImages}>
                      이미지 삭제
                    </Button>
                  ) : null}
                </Box>
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
            </Box>
          </Container>
        </ContentsDiv>
      </Typography>
    </>
  );
};

export default ReviewCreateItem;
