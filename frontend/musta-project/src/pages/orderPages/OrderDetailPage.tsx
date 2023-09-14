import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { Box, Container, Grid, Paper, Table, TableBody, TableCell, TableRow, Typography } from '@mui/material';
import { getOrderHandler } from '../../store/auth-action';
import { getFormattedDate, getFormattedTime } from '../../util/dateUtil';
import axiosUtils from '../../uitls/axiosUtils';
import { formatPrice } from "../../util/formatPrice.ts";

import BackButton from '../../components/BackButton';
import OrderStatusBadge from '../../components/order/OrderStatusBadge';
import OrderActionButton from '../../components/order/OrderActionButton';

const StyledTypography = styled(Typography)({
  fontWeight: 500,
  marginTop: '10px',
  fondWeight: 'bold'
});

const StyledTableCell = styled(TableCell)({
  component: "th",
  scope: "row",
  variant: "head",
  sx: { fontWeight: 'bold', width: '20%' },
});

const OrderDetailPage: React.FC = () => {
  const { articleApiId, orderApiId } = useParams();
  const [orderDetailResponse, setOrderDetail] = useState<OrderDetailResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const navigate = useNavigate();
  const [isHovered, setIsHovered] = useState(false);

  const spanStyle = {
    color: isHovered ? '#007bff' : 'inherit',
    textDecoration: isHovered ? 'underline' : 'none',
    cursor: 'pointer',
  };

  useEffect(() => {
    setLoading(true);
    getOrderHandler(articleApiId, orderApiId).then((response: { data: React.SetStateAction<OrderDetailResponse | null>; } | null) => {
      if (response != null) {
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

  const handleMouseEnter = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  if (!orderDetailResponse || loading) {
    return <div>Loading...</div>;
  }

  const handleArticleClick = () => {
    navigate(`/article/detail/${orderDetailResponse.articleApiId}`);
  };

  const time = getFormattedDate(orderDetailResponse.date) + getFormattedTime(orderDetailResponse.date);

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
              주문 정보
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
                <TableCell>{orderDetailResponse.orderApiId}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문한 게시글</StyledTableCell>
                <TableCell>
                  <span
                    onClick={handleArticleClick}
                    onMouseEnter={handleMouseEnter}
                    onMouseLeave={handleMouseLeave}
                    style={spanStyle}
                  >
                    {orderDetailResponse.articleTitle}
                  </span></TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">결제 금액</StyledTableCell>
                <TableCell>{formatPrice(orderDetailResponse.amount)}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문 상태</StyledTableCell>
                <TableCell>
                  <OrderStatusBadge orderStatus={orderDetailResponse.orderStatus} />
                </TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문 시간</StyledTableCell>
                <TableCell>{time}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">판매자 이름</StyledTableCell>
                <TableCell>{orderDetailResponse.sellerName}</TableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell variant="head">주문자 이름</StyledTableCell>
                <TableCell>{orderDetailResponse.consumerName}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>

        <OrderActionButton
          consumerName={orderDetailResponse.consumerName}
          sellerName={orderDetailResponse.sellerName}
          orderStatus={orderDetailResponse.orderStatus}
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
