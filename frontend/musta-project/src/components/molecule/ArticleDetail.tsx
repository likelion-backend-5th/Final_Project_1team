import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Chip,
  Skeleton,
  Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import {
  ArticleImpl,
  getChipColorByArticleStatus,
} from '../../types/article.ts';
import axios from 'axios';
import { styled } from '@mui/material/styles';
import { Flag, ShoppingCart } from '@mui/icons-material';

const baseUrl = 'http://localhost:8080/api/articles/';

function getArticleApiId() {
  const pathnames = location.pathname.split('/');
  return `${pathnames.pop()}`;
}

const StyledArticleDetail = styled('div')({
  display: 'flex',
  justifyContent: 'center',
  padding: '20px',
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

export function ArticleDetail() {
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
      setTimeout(() => setLoading(false), 1500);
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoading(false);
    }
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
            <Typography gutterBottom variant="h4" component="div">
              {article.title}
            </Typography>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'flex-start',
              }}>
              <Chip
                size="small"
                label={article.articleStatus}
                color={getChipColorByArticleStatus(article.articleStatus)}
              />
              <Typography variant="h6">{article.username}</Typography>
            </Box>
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
              }}>
              <Typography gutterBottom variant="body2">
                {article.createdDate}
              </Typography>
              <Box>
                <Button
                  sx={{ mx: 2 }}
                  size="medium"
                  variant="outlined"
                  color="success"
                  startIcon={<ShoppingCart />}>
                  주문하기
                </Button>
                <Button
                  size="medium"
                  variant="outlined"
                  color="warning"
                  startIcon={<Flag />}>
                  신고하기
                </Button>
              </Box>
            </Box>
            <Typography paragraph>{article.description}</Typography>
          </StyledCardContent>
          <StyledCardActions>
            <Button size="large" startIcon={<ShoppingCart />}>
              주문하기
            </Button>
          </StyledCardActions>
        </StyledCard>
      )}
    </StyledArticleDetail>
  );
}
