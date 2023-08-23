import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import { Paper, Typography, Grid, Chip } from '@mui/material';

const fetchOrderDetail = async (orderApiId: string): Promise<OrderDetail> => {
    // 여기서 실제 API 호출을 구현합니다.
    // 예시로 더미 데이터를 반환하도록 하겠습니다.
    return {
        articleApiId : 'qwerty',
        orderApiId: 'qwer',
        sellerName: 'Seller A',
        consumerName: 'Consumer X',
        date: '2023-08-21',
        productName: 'Product X',
        orderStatus: 'Progress',
        // ... 다른 필드들
    };
};

const StyledPaper = styled(Paper)`
  padding: 16px;
  margin: 16px;
`;

const SectionTitle = styled(Typography)`
  margin-bottom: 8px;
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
        <div>
            <StyledPaper elevation={3}>
                <Typography variant="h4">Order Detail</Typography>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <SectionTitle variant="subtitle1">Order ID:</SectionTitle>
                        <Typography>{orderDetail.orderApiId}</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <SectionTitle variant="subtitle1">Seller Name:</SectionTitle>
                        <Typography>{orderDetail.sellerName}</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <SectionTitle variant="subtitle1">Consumer Name:</SectionTitle>
                        <Typography>{orderDetail.consumerName}</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <SectionTitle variant="subtitle1">Date:</SectionTitle>
                        <Typography>{orderDetail.date}</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <SectionTitle variant="subtitle1">Product Name:</SectionTitle>
                        <Typography>{orderDetail.productName}</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <SectionTitle variant="subtitle1">Order Status:</SectionTitle>
                        <Chip label={orderDetail.orderStatus} color="primary" />
                    </Grid>
                    {/* ... 다른 필드들 */}
                </Grid>
            </StyledPaper>
        </div>
    );
};

export default OrderDetailPage;
