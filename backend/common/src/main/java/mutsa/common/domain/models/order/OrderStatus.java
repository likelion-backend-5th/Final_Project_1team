package mutsa.common.domain.models.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    SALE, BUY, PROGRESS, END,
    ;

    public static OrderStatus of(String s) {
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
