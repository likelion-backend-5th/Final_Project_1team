package mutsa.api.service.report;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.report.ReportStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.report.ReportRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@Slf4j
class ReportModuleServiceTest {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportModuleService reportModuleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private User reporter;
    private User reportedUser;
    private Article targetArticle;

    @BeforeEach
    public void init() {
        reporter = User.of("reporter", "password", "email_reporter", "oauthName", null, null);
        reporter = userRepository.save(reporter);

        reportedUser = User.of("reported", "password", "email_reported", "oauthName", null, null);
        reportedUser = userRepository.save(reportedUser);

        targetArticle = Article.builder().title("test article title")
                .description("test article description")
                .user(reportedUser)
                .build();
        targetArticle = articleRepository.save(targetArticle);
    }

    @Test
    void createReport() {
        //given
        String content = "test report content";
        String resourceType = "article";

        //when
        Report report = reportModuleService.createReport(reporter.getUsername(), resourceType, targetArticle.getApiId(), content);

        //then
        assertThat(report.getReporter().getApiId()).isEqualTo(reporter.getApiId());
        assertThat(report.getReportedUser().getApiId()).isEqualTo(reportedUser.getApiId());
    }

    @Test
    void getAllReports() {
        //given
        String content1 = "test report content 1";
        String content2 = "test report content 2";
        String resourceType = "article";
        reportModuleService.createReport(reporter.getUsername(), resourceType, targetArticle.getApiId(), content1);
        reportModuleService.createReport(reporter.getUsername(), resourceType, targetArticle.getApiId(), content2);

        //when
        List<Report> allReports = reportModuleService.getAllReports();

        //then
        assertThat(allReports.size()).isEqualTo(2);
    }

    @Test
    void getReportByApiId() {
        //given
        String content = "test report content";
        String resourceType = "article";
        Report report = reportModuleService.createReport(reporter.getUsername(), resourceType, targetArticle.getApiId(), content);

        //when
        Report reportByApiId = reportModuleService.getReportByApiId(report.getApiId());

        //then
        assertThat(reportByApiId.getContent()).isEqualTo(content);
    }

    @Test
    void updateReportStatus() {
        //given
        String content = "test report content";
        String resourceType = "article";
        Report report = reportModuleService.createReport(reporter.getUsername(), resourceType, targetArticle.getApiId(), content);
        ReportUpdateStatusDto updateDto = new ReportUpdateStatusDto();
        updateDto.setStatus(ReportStatus.RESOLVED);

        //when
        Report updatedReport = reportModuleService.updateReportStatus(report.getApiId(), updateDto);

        //then
        assertThat(updatedReport.getStatus()).isEqualTo(ReportStatus.RESOLVED);
    }

    @Test
    void deleteByApiId() {
        //given
        String content = "test report content";
        String resourceType = "article";
        Report report = reportModuleService.createReport(reporter.getUsername(), resourceType, targetArticle.getApiId(), content);

        //when
        reportModuleService.deleteByApiId(report.getApiId());

        //then
        Optional<Report> byApiId = reportRepository.findByApiId(report.getApiId());
        assertThat(byApiId.isPresent()).isFalse();
    }
}
