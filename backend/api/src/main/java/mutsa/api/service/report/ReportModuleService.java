package mutsa.api.service.report;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.report.ReportRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportModuleService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public Report createReport(String username, String reportedApiId, String content) {
        User reporter = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User reportedUser = userRepository.findByApiId(reportedApiId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Report report = Report.of(reporter, reportedUser, content);
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportByApiId(String reportApiId) {
        return reportRepository.findByApiId(reportApiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));
    }

    @Transactional
    public Report updateReportStatus(String reportApiId, ReportUpdateStatusDto updateDto) {
        Report report = getReportByApiId(reportApiId);
        report.setStatus(updateDto.getStatus());
        return report;
    }

    @Transactional
    public void deleteByApiId(String reportApiId) {
        Report report = getReportByApiId(reportApiId);
        reportRepository.delete(report);
    }
}
