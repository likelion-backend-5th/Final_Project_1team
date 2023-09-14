import React from 'react';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import PendingIcon from '@mui/icons-material/Pending';
import ClearIcon from '@mui/icons-material/Clear';
import HourglassTopIcon from '@mui/icons-material/HourglassTop';
import {green} from '@mui/material/colors';

interface OrderStatusBadgeProps {
  orderStatus: string;
}

const OrderStatusIcon: React.FC<OrderStatusBadgeProps> = ({ orderStatus }) => {
  if (orderStatus === 'END') {
    return <CheckCircleIcon color="primary" />;
  } else if (orderStatus === 'PROGRESS') {
    return <PendingIcon color="secondary" />;
  } else if (orderStatus === 'CANCEL') {
    return <ClearIcon color="error" />;
  } else if (orderStatus === 'WAIT') {
    return <HourglassTopIcon style={{ color: green[800] }} />;
  } else {
    console.error('Unknown order status');
  }
};

export default OrderStatusIcon;

