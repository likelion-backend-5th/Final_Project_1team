package mutsa.api.dto.report;

import lombok.Getter;
import lombok.Setter;
import mutsa.common.domain.models.report.ReportStatus;

@Getter
@Setter
public class ReportUpdateStatusDto {
    private ReportStatus status;
}
