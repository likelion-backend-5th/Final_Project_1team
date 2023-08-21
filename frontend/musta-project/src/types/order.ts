type OrderStatus = 'Progress' | 'End' | 'Cancled';

interface Order {
  id : number;
  sellerName : string;
  date : string;
  productName: string;
  status: OrderStatus;
}