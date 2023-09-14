import { Rating, styled } from '@mui/material';
import { TextField, Button, Container, Typography, Box } from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';
import EditIcon from '@mui/icons-material/Edit';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { updateReview } from '../../store/auth-action';
import { Carousel } from 'react-responsive-carousel';
import axiosUtils from '../../uitls/axiosUtils';
import { HideImage, InsertPhoto } from '@mui/icons-material';
import { uploadImagesToS3 } from '../../util/s3Client';

const ContentsDiv = styled('div')`
  align: left;
`;

const ReviewEditItem = ({ review }: any) => {
  const [editReview, setEditReview] = useState(review);
  const [imageFiles, setImageFiles] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);
  const [oldImageFiles, setOldImageFiles] = useState([]);

  const navigate = useNavigate();

  const fetchData = async () => {
    axiosUtils.get(`/review/${review.apiId}`).then((response: any) => {
      setOldImageFiles(response.data.images);
    });
  };
  useEffect(() => {
    fetchData();
  }, []);

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

  const handleImageChange = (event: any) => {
    let newImageFiles: any = Array.from(event.target.files);

    if (newImageFiles.length > 5) {
      alert('이미지 갯수는 최대 5개까지만 가능합니다.');
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
    uploadImagesToS3(imageFiles, 'review').then((result) => {
      if (result) {
        try {
          updateReview(
            editReview.apiId,
            editReview.content,
            editReview.point,
            result
          )
            .then((response: any) => {
              navigate(`/review/${response.data.apiId}`); // 수정 후 리뷰 페이지로 이동
            })
            .catch((error: any) => {
              console.error('Error updating review:', error);
            });
        } catch (error) {
          console.error('Error updating review:', error);
        }
      }
    });
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
              <h4 style={{ borderBottom: '1px solid black' }}>내용 수정</h4>
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
                value={Number(editReview.point)}
                sx={{ marginTop: 2 }}
                onChange={(_event, newValue) => handleRatingChange(newValue)}
              />
              <h4 style={{ borderBottom: '1px solid black' }}>기존 이미지</h4>
              {oldImageFiles.length == 0 ? (
                <img
                  style={{ maxWidth: '400px' }}
                  src="/img/favicon.png"
                  title="예시 이미지"
                  alt="no Image"></img>
              ) : (
                <Box style={{ textAlign: 'center' }}>
                  <Carousel
                    showArrows={true}
                    infiniteLoop={true}
                    selectedItem={0}>
                    {oldImageFiles.map((preview: any, index: any) => (
                      <div
                        key={index}
                        style={{
                          display: 'flex',
                          justifyContent: 'center',
                          alignItems: 'center',
                          height: '300px', // Set the desired height
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
              <h4 style={{ borderBottom: '1px solid black' }}>추가된 이미지</h4>
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
                    color="warning"
                    startIcon={<EditIcon />}
                    onClick={handleSaveReview}>
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
            </Box>
          </Container>
        </ContentsDiv>
      </Typography>
    </>
  );
};

export default ReviewEditItem;
