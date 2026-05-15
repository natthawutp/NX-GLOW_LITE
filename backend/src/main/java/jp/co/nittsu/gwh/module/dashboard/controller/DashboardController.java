package jp.co.nittsu.gwh.module.dashboard.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.dashboard.dto.DashboardProductivityTrendDto;
import jp.co.nittsu.gwh.module.dashboard.dto.DashboardSummaryDto;
import jp.co.nittsu.gwh.module.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Dashboard REST controller.
 * Provides aggregated KPIs, status charts, and operational metrics.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getSummary() {
        DashboardSummaryDto summary = dashboardService.getSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @GetMapping("/productivity-trend")
    public ResponseEntity<ApiResponse<DashboardProductivityTrendDto>> getInboundProductivityTrend(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        DashboardProductivityTrendDto trend = dashboardService.getInboundProductivityTrend(dateFrom, dateTo);
        return ResponseEntity.ok(ApiResponse.success(trend));
    }
}
