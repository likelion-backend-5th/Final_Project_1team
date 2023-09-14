package mutsa.api.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRegisterDto {
    private String resourceType;    // 리소스 타입 ("article", "review", "chat" 등)
    private String resourceApiId;   // 해당 리소스의 ApiID
    private String content;         // 신고 내용
}
