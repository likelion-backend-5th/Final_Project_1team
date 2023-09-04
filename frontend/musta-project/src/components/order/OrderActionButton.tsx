import React from 'react';
import {Button} from '@mui/material';

interface OrderActionButtonProps {
    orderStatus: string;
    handleReviewClick: () => void;
    handleOrderCancellation: () => void;
    handleOrderCompletionWithWaiting: () => void;
    handleOrderEnd: () => void;
}

const OrderActionButton: React.FC<OrderActionButtonProps> = ({
    orderStatus,
    handleReviewClick,
    handleOrderCancellation,
    handleOrderCompletionWithWaiting,
    handleOrderEnd,
}) => {
    return (
        <div>
            {orderStatus === 'END' && (
                <Button variant="contained" color="primary" onClick={handleReviewClick}>
                    리뷰 작성하기
                </Button>
            )}

            {orderStatus === 'PROGRESS' && (
                <div>
                    <Button variant="contained" color="primary" onClick={handleOrderCancellation}>
                        주문 취소
                    </Button>
                    <Button variant="contained" color="primary" onClick={handleOrderCompletionWithWaiting}>
                        주문 확인(주문 대기)
                    </Button>
                </div>
            )}

            {orderStatus === 'WAIT' && (
                <Button variant="contained" color="primary" onClick={handleOrderEnd}>
                    주문 완료
                </Button>
            )}
        </div>
    );
};

export default OrderActionButton;
