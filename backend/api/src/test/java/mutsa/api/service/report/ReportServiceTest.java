package mutsa.api.service.report;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.dto.report.ReportRegisterDto;
import mutsa.api.dto.report.ReportResponseDto;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.report.ReportStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
@Transactional
@Slf4j
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

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
//        reporter = userRepository.findByEmail("reporterEmail")
//                .orElseGet(() -> {
//                    User newUser = User.of("reporter", "password", "reporterEmail", null, null, null);
//                    return userRepository.save(newUser);
//                });
//
//        reported = userRepository.findByEmail("reportedEmail")
//                .orElseGet(() -> {
//                    User newUser = User.of("reported", "password", "reportedEmail", null, null, null);
//                    return userRepository.save(newUser);
//                });
    }

    @Test
    void createReport() {
        //given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");
        requestDto.setResourceType("article");
        requestDto.setResourceApiId(targetArticle.getApiId());

        //when
        ReportResponseDto reportResponseDto = reportService.createReport(reporter.getUsername(), requestDto);

        //then
        assertThat(reportResponseDto.getContent()).isEqualTo(requestDto.getContent());
        assertThat(reportResponseDto.getReportedName()).isEqualTo(reportedUser.getUsername());
    }

    @Test
    void getAllReports() {
        // given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");
        requestDto.setResourceType("article");
        requestDto.setResourceApiId(targetArticle.getApiId());

        reportService.createReport(reporter.getUsername(), requestDto);

        //when
        List<ReportResponseDto> allReports = reportService.getAllReports();

        //then
        assertThat(allReports.size()).isGreaterThan(0);
    }

    @Test
    void getReport() {
        //given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");
        requestDto.setResourceType("article");
        requestDto.setResourceApiId(targetArticle.getApiId());

        ReportResponseDto savedReport = reportService.createReport(reporter.getUsername(), requestDto);

        //when
        ReportResponseDto fetchedReport = reportService.getReport(savedReport.getApiId());

        //then
        assertThat(fetchedReport.getApiId()).isEqualTo(savedReport.getApiId());
    }

    @Test
    void updateReport() {
        //given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");
        requestDto.setResourceType("article");
        requestDto.setResourceApiId(targetArticle.getApiId());

        ReportResponseDto savedReport = reportService.createReport(reporter.getUsername(), requestDto);
        ReportUpdateStatusDto updateDto = new ReportUpdateStatusDto();
        updateDto.setStatus(ReportStatus.DISMISSED);

        //when
        ReportResponseDto updatedReport = reportService.updateReport(savedReport.getApiId(), updateDto);

        //then
        assertThat(updatedReport.getStatus()).isEqualTo(updateDto.getStatus());
    }

    @Test
    void deleteReport() {
        //given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");
        requestDto.setResourceType("article");
        requestDto.setResourceApiId(targetArticle.getApiId());
        ReportResponseDto savedReport = reportService.createReport(reporter.getUsername(), requestDto);

        //when
        reportService.deleteReport(savedReport.getApiId());

        //then
        assertThrows(BusinessException.class, () -> {
            reportService.getReport(savedReport.getApiId());
        });
    }
}

