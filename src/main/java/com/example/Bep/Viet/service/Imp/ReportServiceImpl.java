package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.enums.ReportStatus;
import com.example.Bep.Viet.enums.TargetType;
import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.Report;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.ReportRepository;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.request.ReportRequest;
import com.example.Bep.Viet.request.UpdateReportStatusRequest;
import com.example.Bep.Viet.response.ReportResponse;
import com.example.Bep.Viet.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository repository;
    private final UserRepository userRepository;
    @Override
    public ReportResponse createReport(Long userId, ReportRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        Report report = Report.builder()
                .user(user)
                .targetId(request.getTargetId())
                .targetType(request.getTargetType())
                .reason(request.getReason())
                .reportStatus(ReportStatus.PENDING)
                .build();
        return mapToResponse(repository.save(report));
    }

    @Override
    public List<ReportResponse> getAll() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ReportResponse> getReportByStatus(ReportStatus reportStatus) {
        return repository.findByReportStatus(reportStatus).stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ReportResponse> getReportByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ReportResponse> getReportsByTarget(Long targetId, TargetType targetType) {
        return repository.findByTargetIdAndTargetType(targetId,targetType).stream().map(this::mapToResponse).toList();
    }

    @Override
    public ReportResponse getReportById(Long id) {
        return mapToResponse(findById(id));
    }

    @Override
    public ReportResponse updateReportStatus(Long id, UpdateReportStatusRequest request) {
        Report report = findById(id);
        report.setReportStatus(request.getStatus());
        return mapToResponse(repository.save(report));
    }

    @Override
    public void deleteReport(Long id) {
        Report report = findById(id);
        repository.delete(report);
    }
    //helper
    private Report findById(Long id){
        return repository.findById(id).orElseThrow(()->new AppException(ErrorCode.REPORT_NOT_FOUND));
    }

    private ReportResponse mapToResponse(Report report){
        return ReportResponse.builder()
                .id(report.getId())
                .userId(report.getUser().getId())
                .email(report.getUser().getEmail())
                .targetId(report.getTargetId())
                .targetType(report.getTargetType())
                .reason(report.getReason())
                .status(report.getReportStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
