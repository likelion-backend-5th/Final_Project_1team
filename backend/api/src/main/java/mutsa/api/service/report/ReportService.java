package mutsa.api.service.report;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.report.ReportRegisterDto;
import mutsa.api.dto.report.ReportResponseDto;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.common.domain.models.report.Report;
import mutsa.common.domain.models.report.ReportStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReportService {
    private final ReportModuleService reportModuleService;

    public ReportResponseDto createReport(String username, ReportRegisterDto requestDto) {
        Report report = reportModuleService.createReport(username, requestDto.getResourceType(), requestDto.getResourceApiId(), requestDto.getContent());
        return ReportResponseDto.of(report);
    }

    public List<ReportResponseDto> getAllReports() {
        return reportModuleService.getAllReports().stream()
                .map(ReportResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<ReportResponseDto> getReportsByStatus(ReportStatus status) {
        return reportModuleService.getReportsByStatus(status).stream()
                .map(ReportResponseDto::of)
                .collect(Collectors.toList());
    }

    public ReportResponseDto getReport(String reportApiId) {
        Report report = reportModuleService.getReportByApiId(reportApiId);
        return ReportResponseDto.of(report);
    }

    public ReportResponseDto updateReport(String reportApiId, ReportUpdateStatusDto updateDto) {
        Report report = reportModuleService.updateReportStatus(reportApiId, updateDto);
        return ReportResponseDto.of(report);
    }

    public void deleteReport(String reportApiId) {
        reportModuleService.deleteByApiId(reportApiId);
    }
}
