package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.ReportStatus;
import com.example.Bep.Viet.enums.TargetType;
import com.example.Bep.Viet.request.ReportRequest;
import com.example.Bep.Viet.request.UpdateReportStatusRequest;
import com.example.Bep.Viet.response.ReportResponse;

import java.util.List;

public interface ReportService {
    ReportResponse createReport(Long userId,ReportRequest request);
    List<ReportResponse> getAll();
    List<ReportResponse> getReportByStatus(ReportStatus reportStatus);
    List<ReportResponse> getReportByUser(Long userId);
    List<ReportResponse> getReportsByTarget(Long targetId, TargetType targetType);
    ReportResponse getReportById(Long id);
    ReportResponse updateReportStatus(Long id, UpdateReportStatusRequest request);
    void deleteReport(Long id);
}
