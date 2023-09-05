import axios from 'axios';

export type PaymentInfo = {
    customerApiId: string;
    amount: number;
    orderId: string;
    orderName: string;
    customerEmail: string;
    customerName: string;
};

const createTokenHeader = (token: string) => {
    return {
        headers: {
            Authorization: 'Bearer ' + token,
        },
    };
};

export const fetchPaymentInfo = async (articleApiId: string, token: string): Promise<PaymentInfo> => {
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/payments/${articleApiId}`, createTokenHeader(token));
        console.log(response);
        return response.data;
    } catch (error) {
        console.error("Error fetching payment info: ", error);
        throw error;
    }
};
