import React from 'react';
import { List, Select, MenuItem, Pagination } from '@mui/material';
import { styled } from '@mui/system';
import { observer } from 'mobx-react';
import { makeObservable, observable, action } from 'mobx';
import OrderSellerWithArticleItem from './OrderSellerItemWithArticle';
import generateDummyData from '../../types/orderdummy';

const StyledList = styled(List)`
  margin-top: 20px;
`;

const ordersPerPageOptions = [5, 10, 15, 20];
const initialPage = 1;

class OrderStore {

  orders: OrderResponse[] = [];

  initializeOrders() {
    const initialOrders = generateDummyData(25);
    this.orders = observable(initialOrders);
  }

  currentPage = initialPage;
  selectedStatus: 'all' | 'Progress' | 'End' | 'Cancled' = 'all';
  ordersPerPage = ordersPerPageOptions[0];
  sortOrder: 'asc' | 'desc' = 'desc';

  constructor() {
    this.initializeOrders();

    makeObservable(this, {
      currentPage: observable,
      selectedStatus: observable,
      ordersPerPage: observable,
      sortOrder: observable,
      orders: observable,
      setCurrentPage: action,
      setSelectedStatus: action,
      setOrdersPerPage: action,
      setSortOrder: action,
      initializeOrders: action
    });
  }

  setCurrentPage(page: number) {
    this.currentPage = page;
  }

  setSelectedStatus(status: 'all' | 'Progress' | 'End' | 'Cancled') {
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

const OrderSellerItemList: React.FC = observer(() => {
  const sortedOrders = [...orderStore.orders].sort((a, b) => {
    const dateComparison = new Date(a.date).getTime() - new Date(b.date).getTime();
    return orderStore.sortOrder === 'asc' ? dateComparison : -dateComparison;
  });

  const filteredOrders = orderStore.selectedStatus === 'all'
    ? sortedOrders
    : sortedOrders.filter(order => order.orderStatus === orderStore.selectedStatus);

  const totalPageCount = Math.ceil(filteredOrders.length / orderStore.ordersPerPage);
  const startIndex = (orderStore.currentPage - 1) * orderStore.ordersPerPage;
  const visibleOrders = filteredOrders.slice(startIndex, startIndex + orderStore.ordersPerPage);

  const handlePageChange = (newPage: any) => {
    orderStore.setCurrentPage(newPage);
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
        {visibleOrders.map((order, index) => (
          <OrderSellerWithArticleItem key={index} order={order} />
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

export default OrderSellerItemList;

