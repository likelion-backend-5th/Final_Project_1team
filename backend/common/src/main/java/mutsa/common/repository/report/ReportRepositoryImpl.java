package mutsa.common.repository.report;

import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.report.ReportStatus;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static mutsa.common.domain.models.report.QReport.report;

public class ReportRepositoryImpl extends QuerydslRepositorySupport implements ReportCustomRepository {
    public ReportRepositoryImpl() {
        super(Report.class);
    }

    @Override
    public List<Report> findAllByStatus(ReportStatus status) {
        return from(report)
                .where(report.status.eq(status))
                .fetch();
    }
}
