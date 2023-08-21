import OrderStatusList from '../components/ArticleOrder';
import { Container, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';

const StyledContainer = styled(Container)`
  margin-top: 20px;
`;

const StyledPaper = styled(Paper)`
  padding: 20px;
`;

const ArticleOrderPage = () => {
  const articleId = '1'; // Replace with actual post ID
  const articleName = '강아지 산책';

  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          {articleName} 게시글의 주문 목록입니다
        </Typography>
        <OrderStatusList postId={articleId} />
      </StyledPaper>
    </StyledContainer>
  );
};

export default ArticleOrderPage;
