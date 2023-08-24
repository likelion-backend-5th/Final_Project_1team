
const generateRandomDate = (): string => {
    const year = 2023;
    const month = Math.floor(Math.random() * 12) + 1;
    const day = Math.floor(Math.random() * 28) + 1;
    const hours = Math.floor(Math.random() * 24);
    const minutes = Math.floor(Math.random() * 60);
    const seconds = Math.floor(Math.random() * 60);
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
};

const generateDummyData = (size: number): OrderResponse[] => {
    const orderStatusOptions: OrderResponse['orderStatus'][] = ['Progress', 'End', 'Cancled'];
    const dummyData: OrderResponse[] = [];

    for (let i = 0; i < size; i++) {
        const order: OrderResponse = {
            articleApiId: 'articleApiId' + i,
            orderApiId: 'orderApiId' + i,
            sellerName: 'sellerName' + i,
            consumerName: 'consumerName' + i,
            date: generateRandomDate(),
            orderStatus: orderStatusOptions[Math.floor(Math.random() * orderStatusOptions.length)],
            articleTitle: 'articleTitle' + i,
        };

        dummyData.push(order);
    }

    return dummyData;
};

export default generateDummyData;

