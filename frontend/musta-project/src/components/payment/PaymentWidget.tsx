import React, {useEffect, useState} from 'react';
import {fetchPaymentInfo, PaymentInfo} from "../../types/payment";
import {useParams} from 'react-router-dom';

const PaymentWidget: React.FC = () => {
    const [paymentInfo, setPaymentInfo] = useState<PaymentInfo | null>(null);
    const {articleApiId} = useParams();
    const clientKey = import.meta.env.VITE_TEST_CLIENT_API_KEY || '';
    const successUrl = import.meta.env.VITE_TOSS_SUCCESS || '';
    const failUrl = import.meta.env.VITE_TOSS_FAIL || '';

    useEffect(() => {
        const fetchInfo = async () => {
            try {
                const info = await fetchPaymentInfo(articleApiId!);
                setPaymentInfo(info);
                console.log(info)
            } catch (error) {
                console.error("Error fetching payment info: ", error);
            }
        };

        fetchInfo();
    }, [articleApiId]);

    useEffect(() => {
        if (!paymentInfo) return;

        const paymentWidget = new window.PaymentWidget(clientKey, paymentInfo.customerApiId);

        paymentWidget.renderPaymentMethods('#payment-method', {value: paymentInfo.amount}, {variantKey: 'DEFAULT'});
        paymentWidget.renderAgreement('#agreement');

        const paymentButton = document.getElementById('payment-button');

        if (paymentButton) {
            paymentButton.addEventListener('click', () => {
                paymentWidget.requestPayment({
                    orderId: paymentInfo.orderId,
                    orderName: paymentInfo.orderName,
                    successUrl: successUrl,
                    failUrl: failUrl,
                    customerEmail: paymentInfo.customerEmail,
                    customerName: paymentInfo.customerName,
                });
            });
        }
    }, [paymentInfo]);

    return (
        <div>
            <div id="payment-method"></div>
            <div id="agreement"></div>
            <button id="payment-button">결제하기</button>
        </div>
    );
};

export default PaymentWidget;
