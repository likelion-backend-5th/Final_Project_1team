import { List, Pagination } from '@mui/material';
import { styled } from '@mui/system';
import { action, makeObservable, observable } from 'mobx';
import { observer } from 'mobx-react';
import { useEffect } from 'react';
import { getConsumerOrderHandler } from '../../store/auth-action';
import SearchInput from '../atoms/SearchInput';
import OrderConsumerItem from './OrderConsumerItem';
import Dropdown from './OrderDropdown';

const StyledList = styled(List)`
  margin-top: 20px;
`;
const PaginationContainer = styled('div')`
  display: flex;
  justify-content: center;
  margin-top: 10px;
`;


const initialPage = 1;

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
class OrderStore {
  orderFilterResponse: OrderFilterResponseDto | null = null;
  loading = false;
  currentPage = initialPage;
  selectedStatus: 'all' | 'PROGRESS' | 'END' | 'CANCEL' | 'WAIT' = 'all';
  ordersPerPage = ordersPerPageOptions[1].value;
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
      setSearchInput: action,
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

  setSearchInput(searchInput: string) {
    this.searchInput = searchInput;
  }
}

const orderStore = new OrderStore();

const fetchData = (newPage: number) => {
  orderStore.setLoading(true);
  getConsumerOrderHandler(
    orderStore.selectedStatus === 'all' ? undefined : orderStore.selectedStatus,
    orderStore.searchInput === '' ? undefined : orderStore.searchInput, // 상태 파라미터 추가
    orderStore.sortOrder,
    newPage - 1,
    orderStore.ordersPerPage
  ).then((response: { data: OrderFilterResponseDto; } | null) => {
    if (response != null) {
      console.log('내가 구매한 주문목록을 불러옴');
      console.log(response.data);
      orderStore.setOrderFilterResponse(response.data);
    }
    orderStore.setLoading(false);
  });
};

const OrderConsumerItemList: React.FC = observer(() => {
  useEffect(() => {
    fetchData(initialPage);
    return () => {};
  }, []);

  const orders: OrderResponse[] =
    orderStore.orderFilterResponse?.orderResponseDtos.content || [];
  const totalPageCount =
    orderStore.orderFilterResponse?.orderResponseDtos.pageable.totalPages || 0;

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

  const handleSearch = (searchInput: string) => {
    orderStore.setSearchInput(searchInput);
    console.log('Search input value:' + searchInput);
    fetchData(orderStore.currentPage);
  };

  return (
    <div>
      <Dropdown value={orderStore.selectedStatus} onChange={handleStatusChange} options={statusOptions} />
      <Dropdown value={orderStore.sortOrder} onChange={handleSortOrderChange} options={sortOrderOptions} />
      <Dropdown value={orderStore.ordersPerPage} onChange={handleOrdersPerPageChange} options={ordersPerPageOptions} />
      <SearchInput onSearch={handleSearch} />


      <StyledList>
        {orders.length === 0 ? (
          <p>아직 판매된 아이템이 없습니다(검색정보가 없습니다)</p>
        ) : (
          orders.map((order, index) => (
            <OrderConsumerItem key={index} order={order} />
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

export default OrderConsumerItemList;
