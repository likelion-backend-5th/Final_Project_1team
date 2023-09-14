import { Container, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';
import OrderSellerItemList from '../../components/order/OrderSellerItemList';

const StyledContainer = styled(Container)`
  margin-top: 20px;
`;

const StyledPaper = styled(Paper)`
  padding: 20px;
`;

const OrderSellerPage = () => {
  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          나의 판매 목록
        </Typography>
        <OrderSellerItemList />
      </StyledPaper>
    </StyledContainer>
  );
};

export default OrderSellerPage;
