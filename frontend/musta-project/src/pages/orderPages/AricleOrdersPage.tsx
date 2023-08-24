import React, { useEffect, useState } from 'react';
import { Container, List, MenuItem, Pagination, Paper, Select, Typography } from '@mui/material';
import { styled } from '@mui/system';
import OrderSellerItem from '../../components/order/OrderSellerItem';
import { useParams } from 'react-router-dom';
import { getArticleOrderHandler } from '../../store/auth-action';

const StyledContainer = styled(Container)`
  margin-top: 20px;
`;

const StyledPaper = styled(Paper)`
  padding: 20px;
`;

const StyledList = styled(List)`
  margin-top: 20px;
`;

const PaginationContainer = styled('div')`
  display: flex;
  justify-content: center;
  margin-top: 10px;
`;


const ArticleOrderPage: React.FC = () => {
  // URL에서 articleId 파라미터 값을 가져옴
  const { articleId } = useParams();
  const articleIdString: string = articleId ?? '';
  console.log(articleIdString);

  const articleName = '강아지 산책';
  const [loading, setLoading] = useState<boolean>(false);

  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [page, setPage] = useState<number>(0);

  useEffect(() => {
    setLoading(true);
    // 컴포넌트가 처음 마운트될 때만 더미 데이터를 생성하여 orders 상태를 초기화
    const token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBcnRpY2xlQ29udHJvbGxlclRlc3RVc2VyMSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvYXV0aC9sb2dpbiIsImF1dGhvcml0aWVzIjpbXX0.fkAwNZ-vvk99ZnsZI-C9pdgrQ3qMjLr1bqLjG8X7sg0'
    getArticleOrderHandler(token, articleIdString, page, undefined).then((response) => {
      if (response != null) {
        console.log("아티클별 주문목록을 불러옴");
        const initialOrders = response.data.content;
        setOrders(initialOrders);
      }
    })
    //const initialOrders = generateDummyData(25);//더미데이터로 주문 목록 생성

    setLoading(false);
  }, [page]);

  const [selectedStatus, setSelectedStatus] = useState<'all' | 'PROGRESS' | 'END' | 'CANCLED'>('all');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

  const filteredOrders = selectedStatus === 'all' ? orders : orders.filter(order => order.orderStatus === selectedStatus);

  const sortedOrders = [...filteredOrders].sort((a, b) => {
    const dateComparison = new Date(a.date).getTime() - new Date(b.date).getTime();
    return sortOrder === 'asc' ? dateComparison : -dateComparison;
  });

  const handlePageChange = (event: React.ChangeEvent<unknown>, newPage: number) => {
    setPage(newPage - 1); // 페이지 번호는 0부터 시작하므로 1을 빼줍니다.
  };

  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          {articleName} 게시글의 주문목록 입니다
        </Typography>


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
        {/* 페이징 컴포넌트 */}
        <PaginationContainer>
          <Pagination count={Math.ceil(orders.length / 10)} page={page + 1} onChange={handlePageChange} />
        </PaginationContainer>
      </StyledPaper>
    </StyledContainer>
  );
};

export default ArticleOrderPage;
