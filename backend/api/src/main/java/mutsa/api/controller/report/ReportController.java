package mutsa.api.controller.report;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.report.ReportRegisterDto;
import mutsa.api.dto.report.ReportResponseDto;
import mutsa.api.dto.report.ReportUpdateStatusDto;
import mutsa.api.service.report.ReportService;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.report.ReportStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 신고 등록
    @PostMapping("/{reportedApiId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReportResponseDto> registerReport(@PathVariable String reportedApiId, @RequestBody ReportRegisterDto requestDto) {
        String username = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(reportService.createReport(username, reportedApiId, requestDto));
    }

    // 모든 신고 조회 (관리자용)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportResponseDto>> getAllReports(@RequestParam(required = false) ReportStatus status) {
        if (status != null) {
            return ResponseEntity.ok(reportService.getReportsByStatus(status));
        }
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // 특정 신고 조회 (관리자용)
    @GetMapping("/{reportApiId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDto> getReport(@PathVariable String reportApiId) {
        return ResponseEntity.ok(reportService.getReport(reportApiId));
    }

    // 신고 상태 업데이트 (관리자용)
    @PutMapping("/{reportApiId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDto> updateReportStatus(@PathVariable String reportApiId, @RequestBody ReportUpdateStatusDto updateDto) {
        return ResponseEntity.ok(reportService.updateReport(reportApiId, updateDto));
    }

    // 신고 삭제 (관리자용)
    @DeleteMapping("/{reportApiId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable String reportApiId) {
        reportService.deleteReport(reportApiId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
