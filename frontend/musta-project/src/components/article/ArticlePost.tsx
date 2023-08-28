import { useState } from 'react';
import {
  Box,
  Card,
  TextField,
  Button,
  styled,
  Stack,
  Alert,
  Collapse,
} from '@mui/material';
import axios from 'axios';
import { InsertPhoto } from '@mui/icons-material';
import EditIcon from '@mui/icons-material/Edit';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import {
  getArticleOrderHandler,
  postArticleHandler,
} from '../../store/auth-action.tsx';
import { useNavigate } from 'react-router-dom';

const StyledArticleDetail = styled('div')({
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  padding: '20px',
  position: 'relative',
});

const StyledCard = styled(Card)({
  width: '100%',
  maxWidth: '800px',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  borderRadius: '10px',
  padding: '20px', // Increased padding
  overflow: 'hidden',
});

const StyledTextField = styled(TextField)({
  marginBottom: '15px',
  width: '100%', // Expand to full width
});

// ... (이전 코드)

export function ArticlePost() {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [imageFiles, setImageFiles] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);
  const [alertOpen, setAlertOpen] = useState(false);
  const navigate = useNavigate();

  const handleImageChange = (event) => {
    let newImageFiles = Array.from(event.target.files);

    if (newImageFiles.length > 5) {
      setAlertOpen(true);
      newImageFiles = newImageFiles.slice(0, 5);
    }

    setImageFiles(newImageFiles.slice(0, 5));
    const newImagePreviews = newImageFiles.map((file) =>
      URL.createObjectURL(file)
    );
    setImagePreviews(newImagePreviews);
  };

  const handleSubmit = async () => {
    // const apiFormData = new FormData();
    // apiFormData.append('title', title);
    // apiFormData.append('description', description);

    //  s3 파일 전송하는 코드
    // const s3FormData = new FormData();
    // imageFiles.forEach((file) => {
    //   apiFormData.append('images', file);
    // });

    try {
      let token = localStorage.getItem('token');

      token = token
        ? token
        : 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBcnRpY2xlQ29udHJvbGxlclRlc3RVc2VyMSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvYXV0aC9sb2dpbiIsImF1dGhvcml0aWVzIjpbXX0.fkAwNZ-vvk99ZnsZI-C9pdgrQ3qMjLr1bqLjG8X7sg0';
      postArticleHandler(token, title, description).then((response) => {
        if (response != null) {
          console.log('Article posted successfully:', response.data);
          navigate(`/article/detail/${response.data.apiId}`, {
            replace: false,
          });
          return;
        }

        console.log('Response Data is null');
      });
    } catch (error) {
      console.error('Error posting article:', error);
    }
  };

  return (
    <StyledArticleDetail>
      <StyledCard>
        <Box>
          <StyledTextField
            id="article-title"
            label="게시글 제목"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </Box>
        <Box>
          <StyledTextField
            id="article-description"
            label="게시글 내용"
            multiline
            rows={4}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Box>
        <Box>
          <Carousel showArrows={true} infiniteLoop={true} selectedItem={0}>
            {imagePreviews.map((preview, index) => (
              <div
                key={index}
                style={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  height: '300px', // Set the desired height
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
              variant="contained"
              color="primary"
              component="span"
              startIcon={<InsertPhoto />}
              sx={{ marginTop: '15px' }}>
              이미지 첨부
            </Button>
          </label>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            startIcon={<EditIcon />}
            sx={{ marginTop: '15px' }}>
            게시글 작성
          </Button>
        </Box>
        <Box sx={{ marginTop: '20px' }}>
          <Collapse in={alertOpen}>
            <Stack>
              <Alert
                severity="error"
                onClose={() => {
                  setAlertOpen(false);
                }}>
                이미지 갯수는 최대 5개까지만 가능합니다.
              </Alert>
            </Stack>
          </Collapse>
        </Box>
      </StyledCard>
    </StyledArticleDetail>
  );
}

export default ArticlePost;
