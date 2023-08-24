package mutsa.api.service.report;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.report.ReportStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.report.ReportRepository;
import mutsa.common.repository.review.ReviewRepository;
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
    private final ArticleRepository articleRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Report createReport(String username, String resourceType, String resourceApiId, String content) {
        User reporter = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 신고 대상 리소스의 타입에 따른 switch로 User 찾기
        User reportedUser = switch (resourceType) {
            case "article" -> articleRepository.findByApiId(resourceApiId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND))
                    .getUser();
            case "review" -> reviewRepository.findByApiId(resourceApiId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND))
                    .getUser();
            default -> throw new BusinessException(ErrorCode.REPORT_INVALID_RESOURCE_TYPE);
        };

        Report report = Report.of(reporter, reportedUser, content);
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getReportsByStatus(ReportStatus status) {
        return reportRepository.findAllByStatus(status);
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
