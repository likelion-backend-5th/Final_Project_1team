import {
  Alert,
  Button,
  Card,
  CardContent,
  CardMedia,
  Collapse,
  Skeleton,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import { ArticleStatus } from '../../types/article.ts';
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

const baseUrl = 'http://localhost:8080/api/articles/';

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
  ['최신순', '1', 'desc'],
  ['오래된순', '2', 'asc'],
];

function mapArticleStatus(articleStatus: ArticleStatus) {
  if (articleStatus === 'LIVE') {
    return '공개';
  }
  if (articleStatus === 'EXPIRED') {
    return '비공개';
  }
}

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
    //  s3 파일 전송하는 코드
    // const s3FormData = new FormData();
    // imageFiles.forEach((file) => {
    //   apiFormData.append('images', file);
    // });

    //  1. 현재 로그인 상태인지 확인한다.
    //      - 로그인이 되어 있다면 다음 단계
    //      - 로그인이 되어 있지 않다면, 로그인 화면으로 이동
    //  2. s3에 파일을 업로드한다.
    //      - 중복 파일인 경우는 아예 고려 X
    //        - 해쉬 등을 사용해서 중복 파일 검사를 시도해볼 수 있지만, 현재는 고려 x
    //  3. 업로드한 파일의 url 리스트를 api 서버에 전송
    //  4. api 서버에서는 전송받은 url 리스트를 db에 저장

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
      const response = await axios.get(url);
      const data = response.data;
      setApiId(data.apiId);
      setTitle(data.title);
      setDescription(data.description);
      setCreatedDate(data.createdDate);
      setArticleStatus(data.articleStatus);
      //  TODO 기존에 있던 이미지 정보를 받아오는 것도 필요
      setOldImageFiles(data.images);
      setImagePreviews(oldImageFiles);
      setPrice(data.price);
      articleStatusListElement.splice(0, articleStatusListElement.length);
      articleStatusListElement.push([
        mapArticleStatus(data.articleStatus as ArticleStatus),
        '1',
        data.articleStatus,
      ]);
      let index = 2;
      for (let i = 0; i < articleStatus.length; i++) {
        if (articleStatus[i] == (data.articleStatus as ArticleStatus)) {
          continue;
        }
        articleStatusListElement.push([
          mapArticleStatus(articleStatus[i] as ArticleStatus),
          index.toString(),
          articleStatus[i],
        ]);
        index++;
      }
      setTimeout(() => setLoading(false), loadingTime);
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoading(false);
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
                    severity="error"
                    onClose={() => {
                      setAlertOpen(false);
                    }}>
                    이미지 갯수는 최대 5개까지만 가능합니다.
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
              <DropDown
                elements={articleStatusListElement}
                setValue={setArticleStatus}
              />
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
