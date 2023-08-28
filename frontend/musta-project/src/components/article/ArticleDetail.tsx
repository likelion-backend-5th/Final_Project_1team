import {
  Backdrop,
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Chip,
  Skeleton,
  SpeedDial,
  SpeedDialAction,
  SpeedDialIcon,
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
import { Api, Edit, Flag, Share, ShoppingCart, Sms } from '@mui/icons-material';
import ReviewListForm from '../review/ReviewListForm.tsx';
import { loadingTime } from '../../util/loadingUtil.ts';
import { useNavigate } from 'react-router-dom';

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

const actions = [
  { icon: <ShoppingCart color="primary" />, name: '주문하기' },
  { icon: <Sms color="primary" />, name: '채팅하기' },
  { icon: <Flag color="primary" />, name: '신고하기' },
  { icon: <Share color="primary" />, name: '공유하기' },
];

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
                display: 'inline-block',
                flexDirection: 'column',
                alignItems: 'start',
                alignContent: 'start',
              }}>
              <Chip
                size="small"
                label={article.articleStatus}
                color={getChipColorByArticleStatus(article.articleStatus)}
              />
              <Typography gutterBottom variant="h4" component="div">
                {article.title}
              </Typography>
            </Box>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center', // Center content horizontally
                marginBottom: '16px', // Add some spacing
              }}>
              <Typography variant="h6">{article.username}</Typography>
              <Typography variant="body2">{article.createdDate}</Typography>
            </Box>
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                marginTop: '16px', // Add spacing from the chip
              }}>
              <StyledSpeedDial
                ariaLabel="SpeedDial openIcon example"
                icon={<SpeedDialIcon openIcon={<Edit />} />}
                onOpen={handleOpen}
                onClose={handleClose}
                open={open}>
                {actions.map((action) => (
                  <SpeedDialAction
                    key={action.name}
                    icon={action.icon}
                    onClick={handleClose}
                  />
                ))}
              </StyledSpeedDial>
            </Box>
            <Typography paragraph>{article.description}</Typography>
          </StyledCardContent>

          <Button
            variant="contained"
            color="primary"
            onClick={handleOrderClick}>
            해당 게시글의 주문 확인
          </Button>

          <ReviewListForm />
        </StyledCard>
      )}
    </StyledArticleDetail>
  );
}
