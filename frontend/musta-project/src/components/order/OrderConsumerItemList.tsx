import { useEffect, useState } from 'react';
import { List, Select, MenuItem, Pagination } from '@mui/material';
import { styled } from '@mui/system';
import OrderConsumerItem from './OrderConsumerItem';
import generateDummyData from '../../types/orderdummy';
import { makeObservable, observable, action } from 'mobx';
import { observer } from 'mobx-react';
import { getConsumerOrderHandler, getSellOrderHandler } from '../../store/auth-action';

const StyledList = styled(List)`
  margin-top: 20px;
`;

const ordersPerPageOptions = [5, 10, 15, 20];
const initialPage = 1;

class OrderStore {

  orderFilterResponse: OrderFilterResponseDto | null = null;
  loading = false;
  currentPage = initialPage;
  selectedStatus: 'all' | 'PROGRESS' | 'END' | 'CANCLED' = 'all';
  ordersPerPage = ordersPerPageOptions[0];
  sortOrder: 'asc' | 'desc' = 'desc';

  constructor() {
    makeObservable(this, {
      currentPage: observable,
      selectedStatus: observable,
      ordersPerPage: observable,
      sortOrder: observable,
      orderFilterResponse: observable,
      loading: observable,
      setCurrentPage: action,
      setSelectedStatus: action,
      setOrdersPerPage: action,
      setSortOrder: action,
      setLoading: action,
      setOrderFilterResponse: action
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

  setSelectedStatus(status: 'all' | 'PROGRESS' | 'END' | 'CANCLED') {
    this.selectedStatus = status;
    this.currentPage = initialPage;
  }

  setOrdersPerPage(perPage: number) {
    this.ordersPerPage = perPage;
    this.currentPage = initialPage;
  }

  setSortOrder(order: 'asc' | 'desc') {
    this.sortOrder = order;
  }
}


const orderStore = new OrderStore();

const fetchData = (newPage: number) => {
  orderStore.setLoading(true);
  const token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBcnRpY2xlQ29udHJvbGxlclRlc3RVc2VyMSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvYXV0aC9sb2dpbiIsImF1dGhvcml0aWVzIjpbXX0.fkAwNZ-vvk99ZnsZI-C9pdgrQ3qMjLr1bqLjG8X7sg0'
  getConsumerOrderHandler(
    token,
    undefined,
    undefined,
    undefined,
    newPage-1,
    orderStore.ordersPerPage
  ).then((response) => {
    if (response != null) {
      console.log("내가 구매한 주문목록을 불러옴");
      console.log(response.data);
      orderStore.setOrderFilterResponse(response.data);
    }
    orderStore.setLoading(false);
  });
};


 const OrderConsumerItemList: React.FC = observer(() => {
  useEffect(() => {
    fetchData(initialPage);
  }, []);

  const orders:OrderResponse[] = orderStore.orderFilterResponse?.orderResponseDtos.content || [];
  const sortedOrders = orders.slice().sort((a, b) => {
    const dateComparison = new Date(a.date).getTime() - new Date(b.date).getTime();
    return orderStore.sortOrder === 'asc' ? dateComparison : -dateComparison;
  });


  const filteredOrders = orderStore.selectedStatus === 'all'
    ? sortedOrders
    : sortedOrders.filter(order => order.orderStatus === orderStore.selectedStatus);

  const totalPageCount = orderStore.orderFilterResponse?.orderResponseDtos.pageable.totalPages;
  const startIndex = (orderStore.currentPage - 1) * orderStore.ordersPerPage;

  const handlePageChange = (event: React.ChangeEvent<unknown>, newPage: number) => {
    orderStore.setCurrentPage(newPage);
    fetchData(newPage);
  };
  
  const handleOrdersPerPageChange = (event: any) => {
    const newPerPage = event.target.value;
    orderStore.setOrdersPerPage(newPerPage);
  };

  const handleSortOrderChange = (event: any) => {
    const newSortOrder = event.target.value;
    orderStore.setSortOrder(newSortOrder);
  };

  return (
    <div>
      <Select
        value={orderStore.selectedStatus}
        onChange={(e) => orderStore.setSelectedStatus(e.target.value as typeof orderStore.selectedStatus)}
      >
        <MenuItem value="all">All</MenuItem>
        <MenuItem value="Progress">Progress</MenuItem>
        <MenuItem value="End">End</MenuItem>
        <MenuItem value="Cancled">Cancled</MenuItem>
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
        <MenuItem value="asc">Ascending</MenuItem>
        <MenuItem value="desc">Descending</MenuItem>
      </Select>
      <StyledList>
        {filteredOrders.map((order, index) => (
          <OrderConsumerItem key={index} order={order} />
        ))}
      </StyledList>
      <div>
        <Pagination
          count={totalPageCount}
          page={orderStore.currentPage}
          onChange={handlePageChange}
        />
      </div>
    </div>
  );
});

export default OrderConsumerItemList;
