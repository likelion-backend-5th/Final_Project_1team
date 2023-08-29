import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Skeleton,
  SpeedDial,
  TextField,
  Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import {
  ArticleImpl,
  ArticleStatus,
  articleStatus,
} from '../../types/article.ts';
import axios from 'axios';
import { styled } from '@mui/material/styles';
import ReviewListForm from '../review/ReviewListForm.tsx';
import { loadingTime } from '../../util/loadingUtil.ts';
import { useNavigate } from 'react-router-dom';
import DropDown from './DropDown.tsx';

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

const StyledCardActions = styled(CardActions)({
  justifyContent: 'center',
  borderTop: '1px solid #f0f0f0',
  padding: '10px 0',
});

const StyledSpeedDial = styled(SpeedDial)({
  position: 'absolute',
  bottom: '16px',
  right: '16px',
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
  const [article, setAritcle] = useState<ArticleImpl>({
    apiId: null,
    title: '게시글 제목',
    description: '게시글 내용',
    username: '작성자',
    thumbnail: null,
    status: 'ACTIVE',
    articleStatus: 'LIVE',
    createdDate: '1970-01-01 12:00:00',
  });
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const fetchData = async () => {
    try {
      const response = await axios.get(url);
      const data = response.data;
      setAritcle({
        apiId: data.apiId,
        title: data.title,
        description: data.description,
        username: data.username,
        thumbnail: data.thumbnail,
        status: data.status,
        articleStatus: data.articleStatus,
        createdDate: data.createdDate,
      });
      //  TODO DEBUG용
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

  const navigate = useNavigate();
  const handleOrderClick = () => {
    const id = getArticleApiId();
    navigate(`/article/${id}/order`);
  };

  useEffect(() => {
    fetchData();
  }, [url]);

  return (
    <StyledArticleDetail>
      {loading ? (
        <Skeleton variant="rounded" width={888.875} height={639.172} />
      ) : (
        <StyledCard>
          <StyledCardMedia
            component="img"
            alt="place holder"
            image="https://via.placeholder.com/1920x1080.png?text=via%20placeholder.com"
          />
          <StyledCardContent>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                alignContent: 'center',
              }}>
              <DropDown elements={articleStatusListElement} />
              <TextField
                id="article-title"
                defaultValue={article.title}
                size="medium"
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
              {/*<Typography variant="h6">{article.username}</Typography>*/}
              <Typography variant="body2">{article.createdDate}</Typography>
            </Box>
            <Box>
              <TextField
                id="article-description"
                defaultValue={article.description}
                size="medium"
                maxRows="5"
                multiline={true}
              />
            </Box>
          </StyledCardContent>
        </StyledCard>
      )}
    </StyledArticleDetail>
  );
}
