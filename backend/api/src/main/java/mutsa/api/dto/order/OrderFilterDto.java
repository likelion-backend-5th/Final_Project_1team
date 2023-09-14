package mutsa.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderFilterDto {
    private String orderStatus; //주문 상태
    private String text; //게시글의 제목, 구매자의 이름
    private String sortOrder;
    private String userType;
    private int page;
    private int limit;
}
