import React from 'react';
import {Button} from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';
import RateReviewIcon from '@mui/icons-material/RateReview';
import HourglassTopIcon from '@mui/icons-material/HourglassTop';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';

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
                <Button
                    style={{ margin: '0.5rem' }}
                    variant="outlined"
                    color="success"
                    startIcon={<RateReviewIcon />}
                    onClick={handleReviewClick}>
                    리뷰 작성하기
                </Button>
            )}

            {orderStatus === 'PROGRESS' && (
                <div>
                    <Button
                        style={{ margin: '0.5rem' }}
                        variant="outlined"
                        color="error"
                        startIcon={<CancelIcon />}
                        onClick={handleOrderCancellation}>
                        주문 취소
                    </Button>
                    <Button
                        style={{ margin: '0.5rem' }}
                        variant="outlined"
                        color="success"
                        startIcon={<HourglassTopIcon />}
                        onClick={handleOrderCompletionWithWaiting}>
                        주문 확인(주문 대기)
                    </Button>
                </div>
            )}

            {orderStatus === 'WAIT' && (
                <Button
                    style={{ margin: '0.5rem' }}
                    variant="outlined"
                    color="success"
                    startIcon={<CheckCircleIcon />}
                    onClick={handleOrderEnd}>
                    주문 완료
                </Button>
            )}
        </div>
    );
};

export default OrderActionButton;
