package com.expenseapproval.service;

import com.expenseapproval.dto.response.DashboardResponse;
import com.expenseapproval.enums.ExpenseStatus;
import com.expenseapproval.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseRepository expenseRepository;

    public DashboardResponse getDashboard() {
        return DashboardResponse.builder()
            .totalApprovedThisMonth(expenseRepository.getTotalApprovedThisMonth())
            .pendingCount(expenseRepository.countByStatus(ExpenseStatus.PENDING))
            .approvedCount(expenseRepository.countByStatus(ExpenseStatus.APPROVED))
            .rejectedCount(expenseRepository.countByStatus(ExpenseStatus.REJECTED))
            .build();
    }
}