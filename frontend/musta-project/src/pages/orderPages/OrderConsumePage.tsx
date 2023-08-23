import { Container, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';
import OrderConsumerItemList from '../../components/order/OrderConsumerItemList';

const StyledContainer = styled(Container)`
  margin-top: 20px;
`;

const StyledPaper = styled(Paper)`
  padding: 20px;
`;

const OrderConsumePage = () => {
  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          나에게 온 주문목록
        </Typography>
        <OrderConsumerItemList />
      </StyledPaper>
    </StyledContainer>
  );
};

export default OrderConsumePage;
