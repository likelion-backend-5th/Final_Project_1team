import { useState } from 'react';
import { List, Select, MenuItem } from '@mui/material';
import { styled } from '@mui/system';
import OrderConsumerItem from './OrderConsumerItem';

const StyledList = styled(List)`
  margin-top: 20px;
`;
export const OrderConsumerItemList = () => {
  const [orders] = useState<OrderConsumer[]>([
    {
      articleApiId:'qwer',
      orderApiId: 'qwer',
      sellerName: 'Seller A',
      date: '2023-08-21',
      articleTitle: 'Product X',
      orderStatus: 'Progress',
    },
    {
      articleApiId:'qwer',
      orderApiId: 'zxcv',
      sellerName: 'Seller B',
      date: '2023-08-20',
      articleTitle: 'Product Y',
      orderStatus: 'End',
    },
    {
      articleApiId:'qwer',
      orderApiId: 'sdfg',
      sellerName: 'Seller C',
      date: '2023-08-19',
      articleTitle: 'Product Z',
      orderStatus: 'Cancled'
    },
    {
      articleApiId:'qwer',
      orderApiId: 'cvbn',
      sellerName: 'Seller D',
      date: '2023-08-18',
      articleTitle: 'Product W',
      orderStatus: 'Progress',
    },
    {
      articleApiId:'qwer',
      orderApiId: 'tyui',
      sellerName: 'Seller E',
      date: '2023-08-17',
      articleTitle: 'Product V',
      orderStatus: 'End',
    },
    {
      articleApiId:'qwer',
      orderApiId: 'zxcvlf',
      sellerName: 'Seller F',
      date: '2023-08-16',
      articleTitle: 'Product U',
      orderStatus: 'Cancled'
    },
  ]);

  const [selectedStatus, setSelectedStatus] = useState<'all' | 'Progress' | 'End' | 'Cancled'>('all');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc'); 

  const filteredOrders = selectedStatus === 'all' ? orders : orders.filter(order => order.orderStatus === selectedStatus);



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
        {filteredOrders.map((order, index) => (
          <OrderConsumerItem key={index} order={order} /> 
        ))}
      </StyledList>
    </div>
  );
};

export default OrderConsumerItemList;
