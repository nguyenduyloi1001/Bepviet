package com.example.Bep.Viet.request;

import com.example.Bep.Viet.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReportStatusRequest {
    @NotNull(message = "Status is required")
    private ReportStatus status;
}
