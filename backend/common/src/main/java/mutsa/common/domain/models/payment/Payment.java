package mutsa.common.domain.models.payment;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.order.Order;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @Column
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false, name = "pay_amount")
    private Long amount;

    @Column(nullable = false)
    private String orderName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    boolean paySuccessYN;

    @Column
    private String paymentKey;  // API 응답 시 받는 결제 Key

    @Column
    private String orderKey;    // API 요청 시 필요한 UUID

    @Column
    private String failReason;

    @Column
    private boolean cancelYN;

    @Column
    private String cancelReason;

    public void setOrder(Order order) {
        this.order = order;
    }

    public void updateAfterSuccess(String paymentKey) {
        this.payType = PayType.CARD;
        this.paymentKey = paymentKey;
        this.paySuccessYN = true;
    }

    public void updateFail(String message, String orderKey) {
        this.orderKey = orderKey;
        this.failReason = message;
    }
}
