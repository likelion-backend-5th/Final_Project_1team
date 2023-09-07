type OrderStatus = 'PROGRESS' | 'END' | 'CANCEL' | 'WAIT';

interface OrderDetailResponse {
  orderApiId: string;
  articleApiId: string;
  articleTitle: string;
  articleDescription: string;
  articleThumbnail: string;
  consumerName: string;
  sellerName: string;
  date: string;
  orderStatus: OrderStatus;
  amount: number;
}
interface OrderResponse {
  orderApiId: string;
  articleApiId: string;
  sellerName: string;
  consumerName: string;
  date: string;
  articleTitle: string;
  orderStatus: OrderStatus;
  amount: number;
}

interface OrderResponseDto {
  content: OrderResponse[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    numberOfElements: number;
  };
}

interface OrderFilter {
  orderStatus: string; //주문 상태
  text: string; //게시글의 제목, 구매자의 이름
  sortOrder: string;
  userType: string;
  page: number;
  limit: number;
}

interface OrderFilterResponseDto {
  orderResponseDtos: OrderResponseDto;
  orderFilter: OrderFilter;
}
