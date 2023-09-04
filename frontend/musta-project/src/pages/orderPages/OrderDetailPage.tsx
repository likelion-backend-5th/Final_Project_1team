import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { Typography, Grid, Card, CardContent } from '@mui/material';
import { getOrderHandler } from '../../store/auth-action';
import { getFormattedDate, getFormattedTime } from '../../util/dateUtil';
import axiosUtils from '../../uitls/axiosUtils';

import BackButton from '../../components/BackButton';
import OrderStatusBadge from '../../components/order/OrderStatusBadge';
import OrderActionButton from '../../components/order/OrderActionButton';

const StyledCard = styled(Card)`
  margin-bottom: 3px;
  border-radius: 8px;
`;

const Label = styled(Typography)({
  fontWeight: 'bold',
  marginBottom: '8px',
});

const Value = styled(Typography)({
  marginBottom: '16px',
});


const OrderDetailPage: React.FC = () => {
  const { articleApiId, orderApiId } = useParams();
  const [orderDetail, setOrderDetail] = useState<OrderResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const navigate = useNavigate();
  useEffect(() => {
    setLoading(true);
    getOrderHandler(articleApiId, orderApiId).then((response: { data: React.SetStateAction<OrderResponse | null>; } | null) => {
      if (response != null) {
        console.log("주문상세 정보를 불러옴");
        setOrderDetail(response.data);
      }
    })

    setLoading(false);
  }, []);

  const handleOrderAction = (action: string) => {
    const url = `/articles/${articleApiId}/order/${orderApiId}`;
    
    switch (action) {
      case 'CANCEL':
        axiosUtils.put(url, { orderStatus: 'CANCEL' });
        break;
      case 'END':
        axiosUtils.put(url, { orderStatus: 'END' });
        break;
      case 'WAIT':
        axiosUtils.put(url, { orderStatus: 'WAIT' });
        break;
      default:
        break;
    }

    navigate(0);
  };
  
  
  if (!orderDetail || loading) {
    return <div>Loading...</div>;
  }

  const time = getFormattedDate(orderDetail.date) + getFormattedTime(orderDetail.date);

  return (
    <StyledCard elevation={3}>
      <CardContent>
        <BackButton />
        <Typography variant="h4">{orderDetail.articleTitle} </Typography>

        <Grid container spacing={3}>
          <Grid item xs={12} md={12}>
            <Label variant="subtitle1">주문 번호</Label>
            <Value>{orderDetail.orderApiId}</Value>
            <Value>
              <OrderStatusBadge orderStatus={orderDetail.orderStatus} />
            </Value>
            <Value>{time}</Value>
          </Grid>
          <Grid item xs={12} md={6}>
            <Label variant="subtitle1">판매자 이름</Label>
            <Value>{orderDetail.sellerName}</Value>
          </Grid>
          <Grid item xs={12} md={6}>
            <Label variant="subtitle1">주문자 이름</Label>
            <Value>{orderDetail.consumerName}</Value>
          </Grid>
        </Grid>

        <OrderActionButton
          orderStatus={orderDetail.orderStatus}
          handleReviewClick={() => navigate(`/article/${articleApiId}/order/${orderApiId}/review`)}
          handleOrderCancellation={() => handleOrderAction('CANCEL')}
          handleOrderCompletionWithWaiting={() => handleOrderAction('WAIT')}
          handleOrderEnd={() => handleOrderAction('END')}
        />
      </CardContent>
    </StyledCard>
  );
};

export default OrderDetailPage;
