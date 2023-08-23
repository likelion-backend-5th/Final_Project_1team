import React from 'react';
import { List, Select, MenuItem, Pagination } from '@mui/material';
import { styled } from '@mui/system';
import { observer } from 'mobx-react';
import { makeObservable, observable, action } from 'mobx';
import OrderSellerWithArticleItem from './OrderSellerItemWithArticle';

const StyledList = styled(List)`
  margin-top: 20px;
`;

const ordersPerPageOptions = [5, 10, 15, 20];
const initialPage = 1;

class OrderStore {
  orders: OrderSellerWithArticle[] = [
    {
      articleApiId: 'qwer',
      articleTitle: '이거시다!',
      orderApiId: 'qwer',
      consumerName: 'Consumer X',
      date: '2023-08-21',
      productName: 'Product X',
      orderStatus: 'Progress',
    },
    {
      articleApiId: 'qwer',
      articleTitle: '이거시다!',
      orderApiId: 'zxcv',
      consumerName: 'Consumer Y',
      date: '2023-08-20',
      productName: 'Product Y',
      orderStatus: 'End',
    },
    {
      articleApiId: 'qwer',
      articleTitle: '이거시다!',
      orderApiId: 'sdfg',
      consumerName: 'Consumer Z',
      date: '2023-08-19',
      productName: 'Product Z',
      orderStatus: 'Cancled'
    },
    {
      articleApiId: 'qwer',
      articleTitle: '이거시다2!',
      orderApiId: 'cvbn',
      consumerName: 'Consumer W',
      date: '2023-08-18',
      productName: 'Product W',
      orderStatus: 'Progress',
    },
    {
      articleApiId: 'qwer',
      articleTitle: '이거시다!',
      orderApiId: 'tyui',
      consumerName: 'Consumer V',
      date: '2023-08-17',
      productName: 'Product V',
      orderStatus: 'End',
    },
    {
      articleApiId: 'qwer',
      articleTitle: '이거시다2!',
      orderApiId: 'zxcvlf',
      consumerName: 'Consumer U',
      date: '2023-08-16',
      productName: 'Product U',
      orderStatus: 'Cancled'
    }
  ];


  currentPage = initialPage;
  selectedStatus: 'all' | 'Progress' | 'End' | 'Cancled' = 'all';
  ordersPerPage = ordersPerPageOptions[0];
  sortOrder: 'asc' | 'desc' = 'desc';

  constructor() {
    makeObservable(this, {
      currentPage: observable,
      selectedStatus: observable,
      ordersPerPage: observable,
      sortOrder: observable,
      setCurrentPage: action,
      setSelectedStatus: action,
      setOrdersPerPage: action,
      setSortOrder: action,
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

