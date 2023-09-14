import {Container, Paper, Typography} from '@mui/material';
import {styled} from '@mui/system';
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
          내가 구매한 목록
        </Typography>
        <OrderConsumerItemList />
      </StyledPaper>
    </StyledContainer>
  );
};

export default OrderConsumePage;
