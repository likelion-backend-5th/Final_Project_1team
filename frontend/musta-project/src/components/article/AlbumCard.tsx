import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Chip,
  Typography,
} from '@mui/material';
import {
  Article,
  ArticleImpl,
  getChipColorByArticleStatus,
} from '../../types/article.ts';
import { useNavigate } from 'react-router-dom';
import { formatPrice } from '../../util/formatPrice.ts';

type AlbumCardProps = {
  article: ArticleImpl;
  style: { margin: string; width: string };
  detail: string;
};

// const theme = createTheme({
//   palette: {
//     primary: blue,
//   },
// });

export const AlbumCard = (props: AlbumCardProps) => {
  const { article, style } = props;
  const navigate = useNavigate();

  return (
    <Card
      sx={{ maxWidth: style.width, margin: style.margin, boxShadow: 6 }}
      onClick={() => {
        navigate(`/article/detail/${props.detail}`, { replace: false });
      }}>
      <CardActionArea>
        <Box
          sx={{
            width: '244px', // 이미지 컨테이너의 가로 너비를 고정합니다.
            height: '244px', // 세로 높이를 자동으로 조절하여 비율을 유지합니다.
            display: 'flex', // Flex 컨테이너로 설정합니다.
            alignItems: 'center', // 수직 정렬을 가운데로 설정합니다.
            justifyContent: 'center', // 수평 정렬을 가운데로 설정합니다.
          }}
        >
          <CardMedia
            component="img"
            image={
              article.images.length === 0
                ? "https://via.placeholder.com/1920x1080.png?text=via%20placeholder.com"
                : article.images[0].fullPath
            }
            sx={{
              width: '244px', // 이미지 컨테이너의 가로 너비를 고정합니다.
              height: 'auto', // 세로 높이를 자동으로 조절하여 비율을 유지합니다.
              objectFit: 'cover', // 이미지를 컨테이너에 맞게 조절합니다.
            }}
            alt="place holder image"
          />
        </Box>
        <CardContent sx={{ paddingTop: 0 }}>
          <Chip
            label={article.articleStatus}
            size="small"
            color={getChipColorByArticleStatus(article.articleStatus)}
            sx={{
              marginBottom: 1,
              color: '#FFFFFF',
              fontWeight: 'bold',
              textTransform: 'uppercase',
              //  투명도
              // backgroundColor: `${theme.palette.primary.main}D8`,
            }}
          />
          <Typography variant="h6" gutterBottom>
            {article.title}
          </Typography>
          <Typography variant="h6">{formatPrice(article.price)}</Typography>
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ marginBottom: 1 }}>
            작성자: {article.username}
          </Typography>
          <Typography noWrap variant="body2" color="text.secondary">
            {article.description.length >= 15
              ? article.description.slice(0, 15) + '...'
              : article.description}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default AlbumCard;
