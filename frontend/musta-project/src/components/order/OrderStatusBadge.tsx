import React from 'react';
import {Chip} from '@mui/material';
import {blue, green, purple, red} from '@mui/material/colors';

interface OrderStatusBadgeProps {
    orderStatus: string;
}

const OrderStatusBadge: React.FC<OrderStatusBadgeProps> = ({ orderStatus }) => {
    const getOrderStatusColor = (status: string) => {
        switch (status) {
            case 'PROGRESS':
                return purple[200];
            case 'END':
                return blue[200];
            case 'CANCEL':
                return red[200];
            case 'WAIT':
                return green[200];
            default:
                return 'default';
        }
    };

    const chipColor = getOrderStatusColor(orderStatus);

    return <Chip label={orderStatus} style={{ backgroundColor: chipColor }} />;
};

export default OrderStatusBadge;
