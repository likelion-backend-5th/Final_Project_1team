package mutsa.common.domain.models.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PROGRESS, END , CANCEL, WAIT
    ;

    public static OrderStatus of(String s) {
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
