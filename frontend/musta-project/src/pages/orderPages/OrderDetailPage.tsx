import React, { useState, useEffect } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { Paper, Typography, Grid, Chip, FormControl, FormControlLabel, FormGroup, Button } from '@mui/material';
import { getOrderHandler } from '../../store/auth-action';

const StyledPaper = styled(Paper)`
  padding: 16px;
  margin: 16px;
`;

const BoldLabel = styled(Typography)`
  font-weight: bold;
  font-size: 2rem;
  color: #3f51b5;
`;

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
    navigate(`/review/create`); //여기에 리뷰 작성 폼 연결하시면 됩니다
  };


  if (!orderDetail || loading) {
    return <div>Loading...</div>;
  }

  return (
    <StyledPaper elevation={3}>
      <Typography variant="h4">Order Detail</Typography>
      <FormControl component="fieldset">
        <FormGroup>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={<Typography>{orderDetail.orderApiId}</Typography>}
                label={<BoldLabel variant="subtitle1">주문 번호</BoldLabel>}
                labelPlacement="top"
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={<Typography>{orderDetail.sellerName}</Typography>}
                label={<BoldLabel variant="subtitle1">판매자 이름 </BoldLabel>}
                labelPlacement="top"
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={<Typography>{orderDetail.consumerName}</Typography>}
                label={<BoldLabel variant="subtitle1">주문자 이름</BoldLabel>}
                labelPlacement="top"
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={<Typography>{orderDetail.date}</Typography>}
                label={<BoldLabel variant="subtitle1">주문 날짜</BoldLabel>}
                labelPlacement="top"
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={<Typography>{orderDetail.articleTitle}</Typography>}
                label={<BoldLabel variant="subtitle1">게시글 제목</BoldLabel>}
                labelPlacement="top"
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={<Chip label={orderDetail.orderStatus} color="primary" />}
                label={<BoldLabel variant="subtitle1">주문 상태</BoldLabel>}
                labelPlacement="top"
              />
            </Grid>
          </Grid>
        </FormGroup>
      </FormControl>

      {orderDetail.orderStatus === 'END' && (
        <Button variant="outlined" color="primary" onClick={handleReviewClick}>
          리뷰 작성하기
        </Button>
      )}
    </StyledPaper>
  );
};

export default OrderDetailPage;