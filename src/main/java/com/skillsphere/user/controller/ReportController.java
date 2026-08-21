package com.skillsphere.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsphere.user.dto.UserReportResponse;
import com.skillsphere.user.service.ReportService;



@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public UserReportResponse getUserReport() {

        return reportService.getUserReport();
    }
}