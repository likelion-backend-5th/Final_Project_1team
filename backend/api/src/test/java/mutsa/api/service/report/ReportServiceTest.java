package mutsa.api.service.report;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.dto.report.ReportRegisterDto;
import mutsa.api.dto.report.ReportResponseDto;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.common.domain.models.report.ReportStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
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

    private User reporter;
    private User reported;

    @BeforeEach
    public void init() {
        reporter = userRepository.findByEmail("reporterEmail")
                .orElseGet(() -> {
                    User newUser = User.of("reporter", "password", "reporterEmail", null, null, null);
                    return userRepository.save(newUser);
                });

        reported = userRepository.findByEmail("reportedEmail")
                .orElseGet(() -> {
                    User newUser = User.of("reported", "password", "reportedEmail", null, null, null);
                    return userRepository.save(newUser);
                });
    }

    @Test
    void createReport() {
        //given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");

        //when
        ReportResponseDto reportResponseDto = reportService.createReport(reporter.getUsername(), reported.getApiId(), requestDto);

        //then
        assertThat(reportResponseDto.getContent()).isEqualTo(requestDto.getContent());
        assertThat(reportResponseDto.getReportedName()).isEqualTo(reported.getUsername());
    }

    @Test
    void getAllReports() {
        // given
        ReportRegisterDto requestDto = new ReportRegisterDto();
        requestDto.setContent("Test content");

        reportService.createReport(reporter.getUsername(), reported.getApiId(), requestDto);

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

        ReportResponseDto savedReport = reportService.createReport(reporter.getUsername(), reported.getApiId(), requestDto);

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

        ReportResponseDto savedReport = reportService.createReport(reporter.getUsername(), reported.getApiId(), requestDto);
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
        ReportResponseDto savedReport = reportService.createReport(reporter.getUsername(), reported.getApiId(), requestDto);

        //when
        reportService.deleteReport(savedReport.getApiId());

        //then
        assertThrows(BusinessException.class, () -> {
            reportService.getReport(savedReport.getApiId());
        });
    }
}

