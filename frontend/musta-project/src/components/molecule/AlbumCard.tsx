import {
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Chip,
  Typography,
} from '@mui/material';
import { Article, getChipColorByArticleStatus } from '../../types/article.ts';

type AlbumCardProps = {
  article: Article;
  style: { margin: string; width: string };
};

// const theme = createTheme({
//   palette: {
//     primary: blue,
//   },
// });

export const AlbumCard = (props: AlbumCardProps) => {
  const { article, style } = props;

  return (
    <Card sx={{ maxWidth: style.width, margin: style.margin }}>
      <CardActionArea>
        <CardMedia
          component="img"
          // height="140"
          image={
            !article.thumbnail
              ? 'https://via.placeholder.com/1920x1080.png?text=via%20placeholder.com'
              : article.thumbnail
          }
          alt="place holder image"
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
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ marginBottom: 1 }}>
            작성자: {article.username}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {article.description}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default AlbumCard;
