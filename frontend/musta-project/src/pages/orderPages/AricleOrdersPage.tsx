import { Container, List, Pagination, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';
import { action, makeObservable, observable } from 'mobx';
import { observer } from 'mobx-react';
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Dropdown from '../../components/order/OrderDropdown';
import OrderSellerItem from '../../components/order/OrderSellerItem';
import { getArticleHandler, getArticleOrderHandler } from '../../store/auth-action';

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

const sortOrderOptions = [
  { value: 'asc', label: '오름차순(오래된순)' },
  { value: 'desc', label: '내림차순(최신순)' },
];

const statusOptions = [
  { value: 'all', label: '모든 주문' },
  { value: 'PROGRESS', label: '주문 대기' },
  { value: 'END', label: '주문 종료' },
  { value: 'WAIT', label: '주문중' },
  { value: 'CANCEL', label: '주문취소' },
];

const ordersPerPageOptions = [
  { value: 5, label: '5개' },
  { value: 10, label: '10개' },
  { value: 15, label: '15개' },
  { value: 20, label: '20개' },
];
const initialPage = 1;

class OrderStore {
  orderResponseDtos: OrderResponseDto | null = null;
  articleId: string | undefined = undefined;
  loading = false;
  currentPage = initialPage;
  selectedStatus: 'all' | 'PROGRESS' | 'END' | 'CANCEL' | 'WAIT' = 'all';
  ordersPerPage = ordersPerPageOptions[1].value;
  sortOrder: 'asc' | 'desc' = 'desc';
  searchInput = '';
  articleName = 'article'
  constructor() {
    makeObservable(this, {
      currentPage: observable,
      selectedStatus: observable,
      ordersPerPage: observable,
      sortOrder: observable,
      orderResponseDtos: observable,
      loading: observable,
      articleId: observable,
      articleName: observable,
      setCurrentPage: action,
      setSelectedStatus: action,
      setOrdersPerPage: action,
      setSortOrder: action,
      setLoading: action,
      setOrderResponseDto: action,
      setArticleId: action,
      setArticleName: action,
    });
  }

  setOrderResponseDto(response: OrderResponseDto) {
    this.orderResponseDtos = response;
  }

  setLoading(flag: boolean) {
    this.loading = flag;
  }

  setCurrentPage(page: number) {
    this.currentPage = page;
  }

  setSelectedStatus(newStatus: 'all' | 'PROGRESS' | 'END' | 'CANCEL' | 'WAIT') {
    this.selectedStatus = newStatus;
  }

  setOrdersPerPage(perPage: number) {
    this.ordersPerPage = perPage;
    this.currentPage = initialPage;
  }

  setSortOrder(order: 'asc' | 'desc') {
    this.sortOrder = order;
  }

  setArticleId(articleId: string) {
    this.articleId = articleId;
  }

  setArticleName(articleName: string) {
    this.articleName = articleName;
  }
}

const fetchArticleOrderData = (newPage: number) => {
  orderStore.setLoading(true);
  getArticleOrderHandler(
    orderStore.articleId,
    orderStore.selectedStatus === 'all' ? undefined : orderStore.selectedStatus,
    orderStore.sortOrder,
    newPage - 1,
    orderStore.ordersPerPage).then((response: { data: OrderResponseDto; } | null) => {
      if (response != null) {
        console.log("게시글의 주문목록을 불러옴");
        console.log(response.data);
        orderStore.setOrderResponseDto(response.data);
      }
      orderStore.setLoading(false);
    });
};

const fetchArticleData = () => {
  orderStore.setLoading(true);
  getArticleHandler(
    orderStore.articleId).then((response: { data: { title: string; }; } | null) => {
      if (response != null) {
        console.log("게시글의 제목을 불러옴");
        console.log(response.data);
        orderStore.articleName = response.data.title;
      }
      orderStore.setLoading(false);
    });
};

const orderStore = new OrderStore();


const ArticleOrderPage: React.FC = observer(() => {
  // URL에서 articleId 파라미터 값을 가져옴
  const { articleId } = useParams();

  useEffect(() => {
    const articleIdString: string = articleId ?? '';
    orderStore.setArticleId(articleIdString);
    fetchArticleOrderData(initialPage);
    fetchArticleData();
  }, []);

  const orders: OrderResponse[] = orderStore.orderResponseDtos?.content || [];
  const totalPageCount = orderStore.orderResponseDtos?.pageable.totalPages || 0;

  const handleStatusChange = (event: any) => {
    const status = event.target.value;
    orderStore.setSelectedStatus(status);
    fetchArticleOrderData(orderStore.currentPage);
  };


  const handlePageChange = (event: any, newPage: number) => {
    orderStore.setCurrentPage(newPage);
    fetchArticleOrderData(newPage);
  };

  const handleOrdersPerPageChange = (event: any) => {
    const perPage = event.target.value;
    orderStore.setOrdersPerPage(perPage);
    fetchArticleOrderData(orderStore.currentPage);
  };

  const handleSortOrderChange = (event: any) => {
    const newSortOrder = event.target.value;
    orderStore.setSortOrder(newSortOrder);
    fetchArticleOrderData(orderStore.currentPage);
  };


  return (
    <StyledContainer maxWidth="md">
      <StyledPaper>
        <Typography variant="h5" gutterBottom>
          {orderStore.articleName} 게시글의 주문목록 입니다
        </Typography>
        <Dropdown value={orderStore.selectedStatus} onChange={handleStatusChange} options={statusOptions} />
        <Dropdown value={orderStore.sortOrder} onChange={handleSortOrderChange} options={sortOrderOptions} />
        <Dropdown value={orderStore.ordersPerPage} onChange={handleOrdersPerPageChange} options={ordersPerPageOptions} />


        <StyledList>
          {orders.length === 0 ? (
            <p>아직 판매된 주문이 없습니다.</p>
          ) : (
            orders.map((order, index) => (
              <OrderSellerItem key={index} order={order} />
            ))
          )}
        </StyledList>

        <PaginationContainer>
          <Pagination
            count={totalPageCount}
            page={orderStore.currentPage}
            onChange={handlePageChange}
          />
        </PaginationContainer>
      </StyledPaper>
    </StyledContainer>
  );
});

export default ArticleOrderPage;
