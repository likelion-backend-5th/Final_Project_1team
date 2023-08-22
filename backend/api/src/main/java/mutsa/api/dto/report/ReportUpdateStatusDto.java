package mutsa.api.dto.report;

import lombok.Data;
import mutsa.common.domain.models.report.ReportStatus;

@Data
public class ReportUpdateStatusDto {
    private ReportStatus status;
}
