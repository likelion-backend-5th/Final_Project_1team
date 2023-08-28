import React, { useState, useEffect } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import {Typography, Grid, Chip, Button, Card, CardContent } from '@mui/material';
import { getOrderHandler } from '../../store/auth-action';
import { getFormattedDate, getFormattedTime } from '../../util/DateUtil';



const StyledCard = styled(Card)`
margin-bottom : 3px,
  borderRadius:  8px,
`;


const Label = styled(Typography)({
  fontWeight: 'bold',
  marginBottom: '8px',
});

const Value = styled(Typography)({
  marginBottom: '16px',
});

const OrderDetailPage: React.FC = () => {
  Navigate
  const { articleApiId, orderApiId } = useParams();
  const [orderDetail, setOrderDetail] = useState<OrderResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    setLoading(true);
    // 컴포넌트가 처음 마운트될 때만 더미 데이터를 생성하여 orders 상태를 초기화
    const token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBcnRpY2xlQ29udHJvbGxlclRlc3RVc2VyMSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvYXV0aC9sb2dpbiIsImF1dGhvcml0aWVzIjpbXX0.fkAwNZ-vvk99ZnsZI-C9pdgrQ3qMjLr1bqLjG8X7sg0'
    getOrderHandler(token, articleApiId, orderApiId).then((response) => {
      if (response != null) {
        console.log("주문상세 정보를 불러옴");
        setOrderDetail(response.data);
      }
    })

    setLoading(false);
  }, []);

  const navigate = useNavigate();

  const handleReviewClick = () => {
    // 임시 URL
    navigate(`/article/${articleApiId}/order/${orderApiId}/review`); //여기에 리뷰 작성 폼 연결하시면 됩니다
  };


  if (!orderDetail || loading) {
    return <div>Loading...</div>;
  }

  const s = getFormattedDate(orderDetail.date) + getFormattedTime(orderDetail.date);

  return (
    <StyledCard elevation={3}>
      <CardContent>
        <Typography variant="h4">{orderDetail.articleTitle} </Typography>

        <Grid container spacing={3}>
          <Grid item xs={12} md={12}>
            <Label variant="subtitle1">주문 번호</Label>
            <Value>{orderDetail.orderApiId}</Value>
            <Value>
              <Chip label={orderDetail.orderStatus} color="primary" />
            </Value>
            <Value>{s}</Value>
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

        {orderDetail.orderStatus === 'END' && (
          <Button variant="contained" color="primary" onClick={handleReviewClick}>
            리뷰 작성하기
          </Button>
        )}
      </CardContent>
    </StyledCard>
  );
};

export default OrderDetailPage;