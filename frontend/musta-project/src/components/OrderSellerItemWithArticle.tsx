import React from 'react';
import { ListItem, ListItemText, Avatar, Typography, Card, Grid } from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import PendingIcon from '@mui/icons-material/Pending';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';
import ClearIcon from '@mui/icons-material/Clear';
interface OrderItemProps {
  order: OrderSellerWithArticle;
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

const OrderSellerWithArticleItem: React.FC<OrderItemProps> = ({ order }) => {
  const navigate = useNavigate();

  const handleItemClick = () => {
    navigate(`/article/${order.articleApiId}/order/${order.orderApiId}`);
  };

  return (
    <StyledListItem onClick={handleItemClick}>
      <UserAvatar alt={order.consumerName} src="/path/to/user-image.jpg" />
      <OrderItemText
        primary={order.articleTitle}
        secondary={order.consumerName}
      />
      <UserInfoWrapper>
        <Typography variant="body2" color="textSecondary">
          {order.orderStatus}
        </Typography>
        <Typography variant="caption" color="textSecondary">
          {order.date}
        </Typography>
      </UserInfoWrapper>
      {order.orderStatus === 'End' ? (
        <CheckCircleIcon color="primary" />
      ) : order.orderStatus === 'Progress' ? (
        <PendingIcon color="secondary" />
      ) : (
        <ClearIcon color="error" />
      )}
    </StyledListItem>
  );
};

export default OrderSellerWithArticleItem;
