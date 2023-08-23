import OrderSellerItemList from '../components/OrderSellerItemList';
import { Container, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';
import { makeObservable, observable, action } from 'mobx';

const StyledContainer = styled(Container)`
  margin-top: 20px;
`;

const StyledPaper = styled(Paper)`
  padding: 20px;
`;


class OrderSellerStore {
  OrderSeller = null;
  loading = false;

  constructor() {
    makeObservable(this, {
      OrderSeller: observable,
    });
  }
}

const OrderStore = new OrderSellerStore();

const OrderSellerPage = () => {
  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          나의 판매 목록
        </Typography>
        <OrderSellerItemList  />
      </StyledPaper>
    </StyledContainer>
  );
};

export default OrderSellerPage;
