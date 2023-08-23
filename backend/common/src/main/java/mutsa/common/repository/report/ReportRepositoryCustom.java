package mutsa.common.repository.report;

import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.report.ReportStatus;

import java.util.List;

public interface ReportRepositoryCustom {
    List<Report> findAllByStatus(ReportStatus status);
}

