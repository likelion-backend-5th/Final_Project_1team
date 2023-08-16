package mutsa.common.repository.report;

import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByApiId(String apiId);
    List<Report> findByReportedUser(User user);
    List<Report> findByReporter(User user);
}
