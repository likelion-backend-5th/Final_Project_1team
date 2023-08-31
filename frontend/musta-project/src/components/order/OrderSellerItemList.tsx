import React, { useEffect } from 'react';
import { List, Select, MenuItem, Pagination, TextField, Button } from '@mui/material';
import { styled } from '@mui/system';
import { observer } from 'mobx-react';
import { makeObservable, observable, action } from 'mobx';
import OrderSellerWithArticleItem from './OrderSellerItemWithArticle';
import { getSellOrderHandler } from '../../store/auth-action';

const StyledList = styled(List)`
  margin-top: 20px;
`;
const PaginationContainer = styled('div')`
  display: flex;
  justify-content: center;
  margin-top: 10px;
`;

const ordersPerPageOptions = [5, 10, 15, 20];
const initialPage = 1;

class OrderStore {

  orderFilterResponse: OrderFilterResponseDto | null = null;
  loading = false;
  currentPage = initialPage;
  selectedStatus: 'all' | 'PROGRESS' | 'END' | 'CANCLE' = 'all';
  ordersPerPage = ordersPerPageOptions[1];
  sortOrder: 'asc' | 'desc' = 'desc';
  searchInput = '';

  constructor() {
    makeObservable(this, {
      currentPage: observable,
      selectedStatus: observable,
      ordersPerPage: observable,
      sortOrder: observable,
      searchInput: observable,
      orderFilterResponse: observable,
      loading: observable,
      setCurrentPage: action,
      setSelectedStatus: action,
      setOrdersPerPage: action,
      setSortOrder: action,
      setLoading: action,
      setOrderFilterResponse: action,
      setSearchInput: action
    });
  }

  setOrderFilterResponse(response: OrderFilterResponseDto) {
    this.orderFilterResponse = response;
  }

  setLoading(flag: boolean) {
    this.loading = flag;
  }

  setCurrentPage(page: number) {
    this.currentPage = page;
  }

  setSelectedStatus(newStatus: 'all' | 'PROGRESS' | 'END' | 'CANCLE') {
    this.selectedStatus = newStatus;
  }

  setOrdersPerPage(perPage: number) {
    this.ordersPerPage = perPage;
    this.currentPage = initialPage;
  }

  setSortOrder(order: 'asc' | 'desc') {
    this.sortOrder = order;
  }

  setSearchInput(searchInput: string) {
    this.searchInput = searchInput;
  }
}

const orderStore = new OrderStore();

const fetchData = (newPage: number) => {
  orderStore.setLoading(true);
  getSellOrderHandler(
    orderStore.selectedStatus === 'all' ? undefined : orderStore.selectedStatus,
    orderStore.searchInput === '' ? undefined : orderStore.searchInput, // 상태 파라미터 추가
    orderStore.sortOrder,
    newPage - 1,
    orderStore.ordersPerPage
  ).then((response: { data: OrderFilterResponseDto; } | null) => {
    if (response != null) {
      console.log("내가 판매한 주문목록을 불러옴");
      console.log(response.data);
      orderStore.setOrderFilterResponse(response.data);
    }
    orderStore.setLoading(false);
  });
};


const OrderSellerItemList: React.FC = observer(() => {
  useEffect(() => {
    fetchData(initialPage);
  }, []);


  const orders: OrderResponse[] = orderStore.orderFilterResponse?.orderResponseDtos.content || [];
  const totalPageCount = orderStore.orderFilterResponse?.orderResponseDtos.pageable.totalPages || 0;


  const handleStatusChange = (event: any) => {
    const status = event.target.value;
    orderStore.setSelectedStatus(status);
    fetchData(orderStore.currentPage);
  };


  const handlePageChange = (event: any, newPage: number) => {
    orderStore.setCurrentPage(newPage);
    fetchData(newPage);
  };

  const handleOrdersPerPageChange = (event: any) => {
    const perPage = event.target.value;
    orderStore.setOrdersPerPage(perPage);
    fetchData(orderStore.currentPage);
  };

  const handleSortOrderChange = (event: any) => {
    const newSortOrder = event.target.value;
    orderStore.setSortOrder(newSortOrder);
    fetchData(orderStore.currentPage);
  };

  const handleChange = (event: any) => {
    const inputSearch = event.target.value;
    orderStore.setSearchInput(inputSearch);
  };

  const handleSearch = () => {
    console.log('Search input value:');
    fetchData(orderStore.currentPage);
  };

  return (
    <div>
      <Select
        value={orderStore.selectedStatus}
        onChange={handleStatusChange}
      >
        <MenuItem value="all">All</MenuItem>
        <MenuItem value="PROGRESS">Progress</MenuItem>
        <MenuItem value="END">End</MenuItem>
        <MenuItem value="CANCEL">Cancel</MenuItem>
      </Select>
      <Select
        value={orderStore.ordersPerPage}
        onChange={handleOrdersPerPageChange}
      >
        {ordersPerPageOptions.map(option => (
          <MenuItem key={option} value={option}>
            {option} per page
          </MenuItem>
        ))}
      </Select>
      <Select
        value={orderStore.sortOrder}
        onChange={handleSortOrderChange}
      >
        <MenuItem value="asc">오름차순(오래된순)</MenuItem>
        <MenuItem value="desc">내림차순(최신순)</MenuItem>
      </Select>

      <TextField
        label="Input Value"
        value={orderStore.searchInput}
        onChange={handleChange}
      />
      <Button variant="contained" onClick={handleSearch}>
        Search
      </Button>

      <StyledList>
         {orders.length === 0 ? (
            <p>아직 판매된 아이템이 없습니다(검색정보가 없습니다)</p>
          ) : (
            orders.map((order, index) => (
              <OrderSellerWithArticleItem key={index} order={order} />
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
    </div>
  );
});

export default OrderSellerItemList;

