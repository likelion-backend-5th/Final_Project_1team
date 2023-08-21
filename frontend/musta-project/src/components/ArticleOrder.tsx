import React, { useState } from 'react';
import { List, Select, MenuItem } from '@mui/material';
import { styled } from '@mui/system';
import OrderItem from './OrderItem'; // Import the OrderItem component

interface Props {
  postId: string;
}

const StyledList = styled(List)`
  margin-top: 20px;
`;

const OrderStatusList: React.FC<Props> = () => {
  const [orders, setOrders] = useState<Order[]>([
    {
      id: 1,
      sellerName: 'Seller A',
      date: '2023-08-21',
      productName: 'Product X',
      status: 'Progress',
    },
    {
      id: 2,
      sellerName: 'Seller B',
      date: '2023-08-20',
      productName: 'Product Y',
      status: 'End',
    },
    {
      id: 3,
      sellerName: 'Seller C',
      date: '2023-08-19',
      productName: 'Product Z',
      status: 'Cancled',
    },
  ]);

  const [selectedStatus, setSelectedStatus] = useState<'all' | 'Progress' | 'End' | 'Cancled'>('all');

  const filteredOrders = selectedStatus === 'all' ? orders : orders.filter(order => order.status === selectedStatus);

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
      <StyledList>
        {filteredOrders.map((order, index) => (
          <OrderItem key={index} order={order} /> 
        ))}
      </StyledList>
    </div>
  );
};

export default OrderStatusList;
