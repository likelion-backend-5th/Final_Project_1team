import {
  Alert,
  Button,
  Card,
  CardContent,
  CardMedia,
  Collapse,
  FormControlLabel,
  Skeleton,
  Stack,
  Switch,
  TextField,
  Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import {
  ArticleStatus,
  checkArticleInputValidation,
} from '../../types/article.ts';
import axios from 'axios';
import { styled } from '@mui/material/styles';
import { loadingTime } from '../../util/loadingUtil.ts';
import { useNavigate } from 'react-router-dom';
import DropDown from './DropDown.tsx';
import { Carousel } from 'react-responsive-carousel';
import { InsertPhoto } from '@mui/icons-material';
import EditIcon from '@mui/icons-material/Edit';
import { putArticleHandler } from '../../store/auth-action.tsx';
import { uploadImagesToS3, bucketName, s3Client } from '../../util/s3Client.ts';
import axiosUtils from '../../uitls/axiosUtils.ts';
import userStore from '../../store/user/userStore.ts';
import useStore from '../../store/useStores.ts';
import useStores from '../../store/useStores.ts';

// const baseUrl = 'http://localhost:8080/api/articles/';
const baseUrl = import.meta.env.VITE_API + '/api/articles/';

function getArticleApiId() {
  const pathnames = location.pathname.split('/');
  return `${pathnames.pop()}`;
}

const StyledArticleDetail = styled('div')({
  display: 'flex',
  flexDirection: 'column', // Align content in column
  alignItems: 'center', // Center content horizontally
  padding: '20px',
  position: 'relative', // Make it relative
});

const StyledCard = styled(Card)({
  width: '100%',
  maxWidth: '800px',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  borderRadius: '10px',
  overflow: 'hidden',
});

const StyledCardMedia = styled(CardMedia)({
  height: '400px',
  maxWidth: '800px',
  margin: '0 auto',
});

const StyledCardContent = styled(CardContent)({
  padding: '20px',
});

const articleStatusListElement = [
  ['공개', '1', 'LIVE'],
  ['비공개', '2', 'EXPIRED'],
];

export function ArticleEdit() {
  const [url, setURL] = useState(baseUrl + getArticleApiId());
  const [loading, setLoading] = useState(true);
  const [imageFiles, setImageFiles] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);
  const [oldImageFiles, setOldImageFiles] = useState([]);
  const [alertOpen, setAlertOpen] = useState(false);
  const [apiId, setApiId] = useState('');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [createdDate, setCreatedDate] = useState('');
  const [articleStatus, setArticleStatus] = useState('');
  const [price, setPrice] = useState(0);
  const [alertMsg, setAlertMsg] = useState<string>();
  const [openLabel, setOpenLabel] = useState<string>();
  const navigate = useNavigate();
  const { userStore, authStore } = useStores();

  const handleImageChange = (event) => {
    let newImageFiles = Array.from(event.target.files);

    if (newImageFiles.length > 5) {
      setAlertMsg('이미지 갯수는 최대 5개까지만 가능합니다.');
      setAlertOpen(true);
      newImageFiles = newImageFiles.slice(0, 5);
    }

    setImageFiles(newImageFiles.slice(0, 5));
    const newImagePreviews = newImageFiles.map((file) =>
      URL.createObjectURL(file)
    );
    setImagePreviews(newImagePreviews);
  };

  const validation = () => {
    const str = checkArticleInputValidation(title, description, price);

    setAlertMsg(str);
    return !str;
  };

  const handleSubmit = async () => {
    if (!validation()) {
      setAlertOpen(true);
      return;
    }

    uploadImagesToS3(imageFiles, 'article').then((result) => {
      if (result) {
        console.log('All images uploaded successfully.');
        console.log(result);
        try {
          putArticleHandler(
            title,
            description,
            apiId,
            articleStatus as ArticleStatus,
            result,
            price
          ).then((response) => {
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
        // 이미지 업로드 후 다른 처리를 수행할 수 있습니다.
      } else {
        console.log('Image upload failed.');
      }
    });
  };

  const fetchData = async () => {
    try {
      axiosUtils.get(`/articles/${getArticleApiId()}`).then((response) => {
        console.log(response);
        userStore.getUserInfo().then((result) => {
          if (result.data.username !== response.data.username) {
            alert('게시글 작성자가 아닙니다.');
            return;
          }
        });
        setApiId(response.data.apiId);
        setTitle(response.data.title);
        setDescription(response.data.description);
        setCreatedDate(response.data.createdDate);
        setArticleStatus(response.data.articleStatus);
        setOldImageFiles(response.data.images);
        setImagePreviews(oldImageFiles);
        setPrice(response.data.price);
        setOpenLabel(
          response.data.articleStatus === 'LIVE' ? '공개' : '비공개'
        );
        setTimeout(() => setLoading(false), loadingTime);
      });
    } catch (error) {
      console.error('Error fetching data:', error);
      alert('존재하지 않는 게시글 입니다.');
      navigate(`/article`, {
        replace: false,
      });
      return;
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <StyledArticleDetail>
      {loading ? (
        <Skeleton variant="rounded" width={888.875} height={639.172} />
      ) : (
        <StyledCard>
          {oldImageFiles.length == 0 ? (
            <StyledCardMedia
              component="img"
              alt="place holder"
              image="https://via.placeholder.com/1920x1080.png?text=via%20placeholder.com"
            />
          ) : (
            <Box>
              <Carousel showArrows={true} infiniteLoop={true} selectedItem={0}>
                {oldImageFiles.map((preview, index) => (
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
          <StyledCardContent>
            <Box sx={{ marginBottom: '20px' }}>
              <Collapse in={alertOpen}>
                <Stack>
                  <Alert
                    sx={{ whiteSpace: 'pre-line', textAlign: 'start' }}
                    severity="error"
                    onClose={() => {
                      setAlertOpen(false);
                    }}>
                    {alertMsg}
                  </Alert>
                </Stack>
              </Collapse>
            </Box>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                alignContent: 'center',
              }}>
              <TextField
                id="article-title"
                defaultValue={title}
                size="medium"
                inputProps={{ maxLength: 100, 'aria-rowcount': 5 }} // Set maximum character length
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                  setTitle(event.target.value);
                }}
                maxRows="1"
              />
              <TextField
                id="article-price"
                defaultValue={price}
                size="medium"
                inputProps={{ min: 0 }} // Set maximum character length
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                  setPrice(parseInt(event.target.value, 10));
                }}
                maxRows="1"
              />
            </Box>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center', // Center content horizontally
                marginBottom: '16px', // Add some spacing
              }}>
              <Typography variant="body2">{createdDate}</Typography>
            </Box>
            <Box>
              <FormControlLabel
                control={
                  <Switch
                    checked={articleStatus === 'LIVE'}
                    onChange={(event, checked) => {
                      if (checked) {
                        setArticleStatus('LIVE');
                        setOpenLabel('공개');
                      } else {
                        setArticleStatus('EXPIRED');
                        setOpenLabel('비공개');
                      }
                    }}
                  />
                }
                label={openLabel}
              />
            </Box>
            <Box display="flex" justifyContent="center" marginTop="10px">
              <TextField
                id="article-description"
                defaultValue={description}
                size="medium"
                inputProps={{ maxLength: 255, 'aria-rowcount': 5 }} // Set maximum character length
                multiline
                fullWidth={true}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                  setDescription(event.target.value);
                }}
                sx={{ marginBottom: '10px' }}
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
          </StyledCardContent>
        </StyledCard>
      )}
    </StyledArticleDetail>
  );
}
