import React, { useState } from 'react';
import { Container, List, MenuItem, Paper, Select, Typography } from '@mui/material';
import { styled } from '@mui/system';
import { useParams } from 'react-router-dom';
import OrderSellerItem from '../components/OrderSellerItem';

const StyledContainer = styled(Container)`
  margin-top: 20px;
`;

const StyledPaper = styled(Paper)`
  padding: 20px;
`;

const StyledList = styled(List)`
  margin-top: 20px;
`;

const ArticleOrderPage: React.FC = () => {
  // URL에서 articleId 파라미터 값을 가져옴
  const { articleId } = useParams();
  const articleName = '강아지 산책';

  // 더미 데이터로 주문 목록 생성
  const orders: OrderSeller[] = [
    {
      articleApiId: 'qwer',
      orderApiId: 'qwer',
      consumerName: 'Consumer X',
      date: '2023-08-21',
      productName: 'Product X',
      orderStatus: 'Progress',
    },
    {
      articleApiId: 'qwer',
      orderApiId: 'zxcv',
      consumerName: 'Consumer Y',
      date: '2023-08-20',
      productName: 'Product Y',
      orderStatus: 'End',
    },
  ];

  const [selectedStatus, setSelectedStatus] = useState<'all' | 'Progress' | 'End' | 'Cancled'>('all');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc'); 

  const filteredOrders = selectedStatus === 'all' ? orders : orders.filter(order => order.orderStatus === selectedStatus);

  const sortedOrders = [...filteredOrders].sort((a, b) => {
    const dateComparison = new Date(a.date).getTime() - new Date(b.date).getTime();
    return sortOrder === 'asc' ? dateComparison : -dateComparison;
  });

  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          {articleName} 게시글의 주문목록 입니다
        </Typography>

        <div>
          <Select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value as typeof selectedStatus)}
          >
            <MenuItem value="all">All</MenuItem>
            <MenuItem value="Progress">Progress</MenuItem>
            <MenuItem value="End">End</MenuItem>
            <MenuItem value="Cancled">Cancled</MenuItem>
          </Select>
          
          <Select
            value={sortOrder}
            onChange={(e) => setSortOrder(e.target.value as typeof sortOrder)}
          >
            <MenuItem value="asc">오래된 순</MenuItem>
            <MenuItem value="desc">최신 순</MenuItem>
          </Select>

          <StyledList>
            {sortedOrders.map((order, index) => (
              <OrderSellerItem key={index} order={order} />
            ))}
          </StyledList>
        </div>
      </StyledPaper>
    </StyledContainer>
  );
};

export default ArticleOrderPage;
