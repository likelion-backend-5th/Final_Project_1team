import axiosUtils from '../uitls/axiosUtils';

export type PaymentInfo = {
    customerApiId: string;
    amount: number;
    orderId: string;
    orderName: string;
    customerEmail: string;
    customerName: string;
};

export const fetchPaymentInfo = async (articleApiId: string): Promise<PaymentInfo> => {
    try {
        const response = await axiosUtils.get(`/v1/payments/${articleApiId}`);
        console.log(response);
        return response.data;
    } catch (error) {
        console.error("Error fetching payment info: ", error);
        throw error;
    }
};
