import { CalendarMonth, HideImage, InsertPhoto } from '@mui/icons-material';
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from '@mui/icons-material/Edit';
import {
  Alert,
  Button,
  Card,
  CardContent,
  CardMedia, Chip,
  Collapse,
  FormControl,
  FormControlLabel,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  Skeleton,
  Stack,
  Switch,
  TextField
} from '@mui/material';
import Box from '@mui/material/Box';
import { styled } from '@mui/material/styles';
import { useEffect, useState } from 'react';
import { Carousel } from 'react-responsive-carousel';
import { useNavigate } from 'react-router-dom';
import { putArticleHandler } from '../../store/auth-action.tsx';
import useStores from '../../store/useStores.ts';
import {
  ArticleStatus,
  checkArticleInputValidation,
} from '../../types/article.ts';
import axiosUtils from '../../uitls/axiosUtils.ts';
import { loadingTime } from '../../util/loadingUtil.ts';
import { uploadImagesToS3 } from '../../util/s3Client.ts';
import { priceValidation, textValidation } from '../../util/validationUtil.ts';

// const baseUrl = 'http://localhost:8080/api/articles/';
const baseUrl = '/articles/';

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
  const {userStore, authStore} = useStores();
  const [errorTitle, setErrorTitle] = useState<boolean>(false);
  const [errorPrice, setErrorPrice] = useState<boolean>(false);
  const [errorDescription, setErrorDescription] = useState<boolean>(false);

  const handleImageChange = (event) => {
    let newImageFiles = Array.from(event.target.files);

    if (newImageFiles.length > 5) {
      setAlertMsg('이미지 갯수는 최대 5개까지만 가능합니다.');
      setAlertOpen(true);
      newImageFiles = newImageFiles.slice(0, 5);
    }

    setImageFiles((newImageFiles as (Blob | MediaSource)[]).slice(0, 5));
    const newImagePreviews = newImageFiles.map((file) =>
      URL.createObjectURL(file as Blob) // 타입 단언을 사용하여 'Blob' 타입으로 변환
    );
    
    setImagePreviews(newImagePreviews);
  };

  const validation = () => {
    const str = checkArticleInputValidation(title, description, price);

    setAlertMsg(str);
    return !str;
  };

  const handleDelete = async() => {
    axiosUtils.delete(baseUrl + getArticleApiId()).then((res) => {
      if (res.status === 200) {
        navigate('/article');
        return;
      }
    }).catch((err) => {
      console.log(err);
    })
  }

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
    axiosUtils
      .get(`/articles/${getArticleApiId()}`)
      .then((response) => {
        console.log(response);
        if (authStore.userInfo?.username !== response.data.username) {
          if (!authStore.userInfo?.role.includes('ROLE_ADMIN')) {
            alert('게시글 작성자가 아닙니다.');
            navigate(`/article`, {
              replace: false,
            });
            return;
          }
        }
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
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
        alert('존재하지 않는 게시글 입니다.');
        navigate(`/article`, {
          replace: false,
        });
        return;
      });
  };

  useEffect(() => {
    fetchData();
  }, []);

  const onChangeTitle = (event: React.ChangeEvent<HTMLInputElement>) => {
    !textValidation(event.target.value)
      ? setErrorTitle(true)
      : setErrorTitle(false);
    setTitle(event.target.value);
  };

  const onChangePrice = (event: React.ChangeEvent<HTMLInputElement>) => {
    const inputValue = event.target.value;
    const priceValue = parseFloat(inputValue); // 문자열을 숫자로 변환
  
    !priceValidation(priceValue)
      ? setErrorPrice(true)
      : setErrorPrice(false);
  
    setPrice(priceValue);
  };

  const onChangeDescription = (event: React.ChangeEvent<HTMLInputElement>) => {
    !textValidation(event.target.value)
      ? setErrorDescription(true)
      : setErrorDescription(false);
    setDescription(event.target.value);
  };

  const onClickRemoveImages = () => {
    setImageFiles([]);
    setImagePreviews([]);
  };

  return (
    <StyledArticleDetail>
      {loading ? (
        <Skeleton variant="rounded" width={888.875} height={639.172}/>
      ) : (
        <StyledCard>
          {oldImageFiles.length == 0 ? (
            <StyledCardMedia
              image="https://via.placeholder.com/1920x1080.png?text=via%20placeholder.com"
              sx={{
                aspectRatio: ' 1/1', // 이미지의 가로세로 비율을 자동으로 조정합니다.
              }}
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
            <Box sx={{marginBottom: '20px'}}>
              <Collapse in={alertOpen}>
                <Stack>
                  <Alert
                    sx={{whiteSpace: 'pre-line', textAlign: 'start'}}
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
              display='flex'
              justifyContent='space-between'
              flexDirection='column'
              sx={{
                marginBottom: '16px', // Add some spacing
              }}>
              <Box>
                <Chip icon={<CalendarMonth/>} sx={{fontSize: '1rem'}} label={createdDate} color={'info'}/>
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
            </Box>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                alignContent: 'center',
              }}>
              <FormControl sx={{marginY: '10px'}}>
                <InputLabel htmlFor="article-price">가격</InputLabel>
                <OutlinedInput
                  id="article-price"
                  defaultValue={price}
                  size="medium"
                  onChange={onChangePrice}
                  label="가격"
                  startAdornment={
                    <InputAdornment position="start">￦</InputAdornment>
                  }
                  value={price}
                  error={errorPrice}
                  maxRows="1"
                />
              </FormControl>
              <TextField
                id="article-title"
                defaultValue={title}
                size="medium"
                label="제목"
                inputProps={{maxLength: 100, 'aria-rowcount': 5}} // Set maximum character length
                onChange={onChangeTitle}
                error={errorTitle}
                maxRows="1"
                sx={{marginY: '10px'}}
                fullWidth
              />
            </Box>
            <Box display="flex" justifyContent="center" marginTop="10px">
              <TextField
                id="article-description"
                defaultValue={description}
                size="medium"
                inputProps={{maxLength: 255, 'aria-rowcount': 5}} // Set maximum character length
                multiline
                rows={5}
                label="내용"
                fullWidth={true}
                error={errorDescription}
                onChange={onChangeDescription}
                sx={{marginBottom: '10px'}}
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
              <Box>
                <input
                  accept="image/*"
                  id="image-file"
                  type="file"
                  multiple
                  onChange={handleImageChange}
                  style={{display: 'none'}}
                />
                <label htmlFor="image-file">
                  <Button
                    variant="contained"
                    color="primary"
                    component="span"
                    startIcon={<InsertPhoto/>}
                    sx={{marginTop: '15px'}}>
                    이미지 첨부
                  </Button>
                </label>
                {imagePreviews.length > 0 ? (
                  <Button
                    variant="contained"
                    color="error"
                    component="span"
                    startIcon={<HideImage/>}
                    onClick={onClickRemoveImages}
                    sx={{marginTop: '15px', marginX: '10px'}}>
                    이미지 삭제
                  </Button>
                ) : null}
              </Box>
              <Box>
                <Button
                  variant="contained"
                  color="error"
                  onClick={handleDelete}
                  startIcon={<DeleteIcon/>}
                  sx={{marginTop: '15px'}}>
                  게시글 삭제
                </Button>
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleSubmit}
                  startIcon={<EditIcon/>}
                  sx={{marginTop: '15px', marginX: '15px'}}>
                  게시글 작성
                </Button>
              </Box>
            </Box>
          </StyledCardContent>
        </StyledCard>
      )}
    </StyledArticleDetail>
  );
}
