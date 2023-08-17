package mutsa.common.domain.models.report;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.user.User;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Table(name = "report")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(unique = true, nullable = false)
    @Builder.Default
    private final String apiId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter; // 신고한 사람

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private User reportedUser; // 신고당한 사람

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public static Report of(User reporter, User reportedUser, String content) {
        return Report.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .content(content)
                .status(ReportStatus.PENDING)
                .build();
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", apiId='" + apiId + '\'' +
                ", reporter=" + reporter +
                ", reportedUser=" + reportedUser +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
