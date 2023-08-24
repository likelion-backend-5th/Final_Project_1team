import { useEffect, useState } from 'react';
import { List, Select, MenuItem } from '@mui/material';
import { styled } from '@mui/system';
import OrderConsumerItem from './OrderConsumerItem';
import generateDummyData from '../../types/orderdummy';

const StyledList = styled(List)`
  margin-top: 20px;
`;
export const OrderConsumerItemList = () => {
  // 더미 데이터로 주문 목록 생성
  const [orders, setOrders] = useState<OrderResponse[]>([]);

  useEffect(() => {
    // 컴포넌트가 처음 마운트될 때만 더미 데이터를 생성하여 orders 상태를 초기화
    const initialOrders = generateDummyData(25);
    setOrders(initialOrders);
  }, []);

  
  const [selectedStatus, setSelectedStatus] = useState<'all' | 'Progress' | 'End' | 'Cancled'>('all');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

  const filteredOrders = selectedStatus === 'all' ? orders : orders.filter(order => order.orderStatus === selectedStatus);

  const sortedOrders = [...filteredOrders].sort((a, b) => {
    const dateComparison = new Date(a.date).getTime() - new Date(b.date).getTime();
    return sortOrder === 'asc' ? dateComparison : -dateComparison;
  });


  return (
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
          <OrderConsumerItem key={index} order={order} />
        ))}
      </StyledList>
    </div>
  );
};

export default OrderConsumerItemList;
