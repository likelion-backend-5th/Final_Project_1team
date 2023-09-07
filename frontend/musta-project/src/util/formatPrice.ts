export const formatPrice = (price) => {
    return new Intl.NumberFormat('ko-KR').format(price) + '원';
};