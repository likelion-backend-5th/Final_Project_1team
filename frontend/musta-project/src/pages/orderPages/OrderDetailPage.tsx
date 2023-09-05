import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import styled from 'styled-components';
import {Box, Container, Grid, Paper, Table, TableBody, TableCell, TableRow, Typography} from '@mui/material';
import {getOrderHandler} from '../../store/auth-action';
import {getFormattedDate, getFormattedTime} from '../../util/dateUtil';
import axiosUtils from '../../uitls/axiosUtils';

import BackButton from '../../components/BackButton';
import OrderStatusBadge from '../../components/order/OrderStatusBadge';
import OrderActionButton from '../../components/order/OrderActionButton';

const StyledTypography = styled(Typography)({
  fontWeight: 700,
  marginTop: '10px',
  color: 'darkblue',
});

const StyledTableCell = styled(TableCell)({
  component: "th",
  scope: "row",
  variant: "head",
  sx: { fontWeight: 'bold', width: '20%' },
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
    <Container>
      <Box sx={{ my: 4 }}>
        <Grid
          container
          justifyContent="space-between"
          alignItems="center"
          spacing={2}
          style={{ marginBottom: '8px' }}
        >
          <Grid item>
            <BackButton />
          </Grid>
          <Grid item>
            <StyledTypography variant="h4" gutterBottom>
              {orderDetail.articleTitle}
            </StyledTypography>
          </Grid>
        </Grid>


        <Paper
          elevation={3}
          style={{ marginBottom: '8px' }}>
          <Table sx={{ minWidth: 650 }}>
            <TableBody>
              <TableRow>
                <StyledTableCell variant="head">주문 번호</StyledTableCell>
                <TableCell>{orderDetail.orderApiId}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문한 게시글</StyledTableCell>
                <TableCell>{orderDetail.articleTitle}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문 상태</StyledTableCell>
                <TableCell>
                  <OrderStatusBadge orderStatus={orderDetail.orderStatus} />
                </TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문 시간</StyledTableCell>
                <TableCell>{time}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">판매자 이름</StyledTableCell>
                <TableCell>{orderDetail.sellerName}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문자 이름</StyledTableCell>
                <TableCell>{orderDetail.consumerName}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>

        <OrderActionButton
          orderStatus={orderDetail.orderStatus}
          handleReviewClick={() => navigate(`/article/${articleApiId}/order/${orderApiId}/review`)}
          handleOrderCancellation={() => handleOrderAction('CANCEL')}
          handleOrderCompletionWithWaiting={() => handleOrderAction('WAIT')}
          handleOrderEnd={() => handleOrderAction('END')}
        />
      </Box>
    </Container>
  );
};

export default OrderDetailPage;
