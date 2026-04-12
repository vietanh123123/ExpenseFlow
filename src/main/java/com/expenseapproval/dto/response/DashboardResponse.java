package com.expenseapproval.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private BigDecimal totalApprovedThisMonth;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;
    private Map<String, BigDecimal> spendingByCategory;
}