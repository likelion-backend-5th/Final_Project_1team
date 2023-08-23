import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import { Paper, Typography, Grid, Chip, FormControl, FormControlLabel, FormGroup } from '@mui/material';

const fetchOrderDetail = async (orderApiId: string): Promise<OrderDetail> => {
  // 여기서 실제 API 호출을 구현, 주문 상세 보기 데이터 가져오기
  return {
    articleApiId: 'qwerty',
    orderApiId: 'qwer',
    sellerName: 'Seller A',
    consumerName: 'Consumer X',
    date: '2023-08-21',
    orderStatus: 'Progress',
    articleTitle: '이거시다!',
  };
};

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
  const { orderApiId } = useParams();
  const [orderDetail, setOrderDetail] = useState<OrderDetail | null>(null);

  useEffect(() => {
    const fetchOrderDetailData = async () => {
      const result = await fetchOrderDetail('qwer');
      setOrderDetail(result);
    };

    fetchOrderDetailData();
  }, [orderApiId]);

  if (!orderDetail) {
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
    </StyledPaper>
  );
};

export default OrderDetailPage;
