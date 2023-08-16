package mutsa.api.service.report;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.report.ReportRegisterDto;
import mutsa.api.dto.report.ReportResponseDto;
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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // 신고 등록
    @Transactional
    public ReportResponseDto createReport(Long reporterId, Long reportedId, ReportRegisterDto requestDto) {
        User reporter = userRepository.findById(reporterId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User reportedUser = userRepository.findById(reportedId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Report report = Report.of(reporter, reportedUser, requestDto.getContent());
        reportRepository.save(report);

        return ReportResponseDto.of(report);
    }

    // 신고 목록 조회
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll().stream()
                .map(ReportResponseDto::of)
                .collect(Collectors.toList());
    }

    // 신고 단일 조회
    public ReportResponseDto getReport(String reportApiId) {
        Report report = reportRepository.findByApiId(reportApiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));

        return ReportResponseDto.of(report);
    }

    // 신고 상태 변경
    @Transactional
    public ReportResponseDto updateReport(String reportApiId, ReportUpdateStatusDto updateDto) {
        Report report = reportRepository.findByApiId(reportApiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));
        report.setStatus(updateDto.getStatus());
        reportRepository.save(report);

        return ReportResponseDto.of(report);
    }

    // 신고 삭제
    @Transactional
    public void deleteReport(String reportApiId) {
        Report report = reportRepository.findByApiId(reportApiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));
        reportRepository.delete(report);
    }
}
