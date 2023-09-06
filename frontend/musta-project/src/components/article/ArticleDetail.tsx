import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
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
import {
  Close,
  Edit,
  Flag,
  Help,
  Share,
  ShoppingCart,
  Sms,
} from '@mui/icons-material';
import ReviewListForm from '../review/ReviewListForm.tsx';
import { loadingTime } from '../../util/loadingUtil.ts';
import { useNavigate } from 'react-router-dom';
import EditIcon from '@mui/icons-material/Edit';
import IconButton from '@mui/material/IconButton';
import { createChatroom, createOrder } from '../../store/auth-action.tsx';
import { Chatroom } from '../../types/chat.ts';

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

const formatPrice = (price) => {
  return new Intl.NumberFormat('ko-KR').format(price) + '원';
};

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
    price: 1000,
  });
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [moveToEditPage, setMoveToEditPage] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleOpenMoveEdit = () => setMoveToEditPage(true);
  const handleCloseMoveEdit = () => setMoveToEditPage(false);

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
        price: data.price,
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

  const handleEditClick = () => {
    const id = getArticleApiId();
    navigate(`/article/edit/${id}`);
  };

  const handleChatRoomClick = (chatRoom: Chatroom) => {
    console.log(chatRoom.chatroomApiId + '을 클릭함');
    navigate(`/chatroom/${chatRoom.chatroomApiId}`);
  };

  const handleCreateOrderClick = (order: OrderDetailResponse) => {
    navigate(`/article/${order.articleApiId}/order/${order.orderApiId}`);
  };

  const actions = [
    {
      icon: <ShoppingCart color="primary" />,
      name: '주문하기',
      onClick: () => {
        console.log('onClick 주문하기');
        createOrder(getArticleApiId()).then((response: { data: OrderDetailResponse; }) => {
          const order: OrderDetailResponse = response.data;
          handleCreateOrderClick(order);
        });
      },
    },
    {
      icon: <Sms color="primary" />,
      name: '채팅하기',
      onClick: () => {
        console.log('onclick 채팅하기');
        createChatroom(getArticleApiId()).then((response: { data: Chatroom; }) => {
          const chatroom: Chatroom = response.data;
          handleChatRoomClick(chatroom);
        });
      },
    },
    {
      icon: <Flag color="primary" />,
      name: '신고하기',
      onClick: () => {
        console.log('onClick 신고하기');
      },
    },
    {
      icon: <Share color="primary" />,
      name: '공유하기',
      onClick: () => {
        console.log('onclick 공유하기');
      },
    },
    {
      icon: <EditIcon color="primary" />,
      name: '수정하기',
      onClick: () => {
        handleOpenMoveEdit();
      },
    },
  ];

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
              <Typography variant="h6" style={{ fontWeight: 'bold', fontSize: '1.5em' }}>
                {formatPrice(article.price)}
              </Typography>
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
                    onClick={action.onClick}
                  />
                ))}
              </StyledSpeedDial>
            </Box>
            <Typography paragraph>{article.description}</Typography>
            <Box>
              <Dialog
                open={moveToEditPage}
                onClose={handleCloseMoveEdit}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description">
                <Box
                  display={'flex'}
                  flexDirection={'row'}
                  justifyContent={'start'}
                  alignItems={'center'}
                  marginX={'10px'}>
                  <Help fontSize={'large'} />
                  <DialogTitle id="alert-dialog-title">{'알림'}</DialogTitle>
                </Box>
                <IconButton
                  aria-label="close"
                  onClick={handleCloseMoveEdit}
                  sx={{
                    position: 'absolute',
                    right: 8,
                    top: 8,
                    color: (theme) => theme.palette.grey[500],
                  }}>
                  <Close />
                </IconButton>
                <DialogContent>
                  <DialogContentText id="alert-dialog-description">
                    게시글 수정 페이지로 이동하시겠습니까?
                  </DialogContentText>
                </DialogContent>
                <DialogActions>
                  <Button onClick={handleCloseMoveEdit}>취소</Button>
                  <Button onClick={handleEditClick} autoFocus>
                    이동
                  </Button>
                </DialogActions>
              </Dialog>
            </Box>
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
