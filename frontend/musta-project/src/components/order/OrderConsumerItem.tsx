import React from 'react';
import { ListItem, ListItemText, Avatar, Typography, Grid } from '@mui/material';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';
import OrderStatusIcon from './OrderStatusIcon';
interface OrderItemProps {
  order: OrderResponse;
}

const StyledListItem = styled(ListItem)`
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  transition: background-color 0.3s;
  cursor: pointer;

  &:hover {
    background-color: rgba(0, 0, 0, 0.05);
  }
`;

const UserAvatar = styled(Avatar)`
  width: 48px;
  height: 48px;
  margin-right: 12px;
`;

const OrderItemText = styled(ListItemText)`
  flex: 1;
`;

const UserInfoWrapper = styled(Grid)`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
`;

const OrderConsumerItem: React.FC<OrderItemProps> = ({ order }) => {
  const navigate = useNavigate();

  const handleItemClick = () => {
    navigate(`/article/${order.articleApiId}/order/${order.orderApiId}`);
  };

  return (
    <StyledListItem onClick={handleItemClick}>
      <UserAvatar alt={order.sellerName} src={order.sellerProfileImage} />
      <OrderItemText
        primary={order.articleTitle}
        secondary={order.sellerName}
      />
      <UserInfoWrapper>
        <Typography variant="body2" color="textSecondary">
          {order.orderStatus}
        </Typography>
        <Typography variant="caption" color="textSecondary">
          {order.date}
        </Typography>
      </UserInfoWrapper>
      <OrderStatusIcon orderStatus={order.orderStatus} />
    </StyledListItem>
  );
};

export default OrderConsumerItem;
