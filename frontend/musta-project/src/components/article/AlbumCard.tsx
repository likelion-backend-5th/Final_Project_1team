import {
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
        <CardMedia
          component="img"
          image={
            article.images.length == 0
              ? 'https://via.placeholder.com/1920x1080.png?text=via%20placeholder'
              : article.images[0].fullPath
          }
          alt="place holder image"
          sx={{
            // Width: '244px',
            // maxHeight: '140px',
            width: '244px',
            height: '137px',
          }}
        />
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
            {article.description}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default AlbumCard;
