package com.expenseapproval.service;

import com.expenseapproval.dto.request.ApprovalRequest;
import com.expenseapproval.dto.response.ExpenseResponse;
import com.expenseapproval.entity.ApprovalAction;
import com.expenseapproval.entity.ExpenseRequest;
import com.expenseapproval.entity.User;
import com.expenseapproval.enums.ExpenseStatus;
import com.expenseapproval.exception.ResourceNotFoundException;
import com.expenseapproval.repository.ApprovalRepository;
import com.expenseapproval.repository.ExpenseRepository;
import com.expenseapproval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ExpenseRepository expenseRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public ExpenseResponse processApproval(Long expenseId, ApprovalRequest request) {
        User currentUser = getCurrentUser();

        ExpenseRequest expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        // Only PENDING expenses can be approved/rejected
        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new RuntimeException("Expense is not in PENDING state");
        }

        // Only APPROVED or REJECTED are valid actions here
        if (request.getAction() != ExpenseStatus.APPROVED
                && request.getAction() != ExpenseStatus.REJECTED) {
            throw new RuntimeException("Invalid action");
        }

        // Update expense status
        expense.setStatus(request.getAction());
        expenseRepository.save(expense);

        // Record the approval action
        ApprovalAction action = ApprovalAction.builder()
            .expenseRequest(expense)
            .actedBy(currentUser)
            .action(request.getAction())
            .comment(request.getComment())
            .build();
        approvalRepository.save(action);

        auditService.log("ExpenseRequest", expense.getId(),
            request.getAction().name(), currentUser.getEmail(), request.getComment());

        // Map to response manually or reuse ExpenseService mapper
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setStatus(expense.getStatus());
        response.setAmount(expense.getAmount());
        return response;
    }

    public ExpenseResponse markAsPaid(Long expenseId) {
        User currentUser = getCurrentUser();

        ExpenseRequest expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (expense.getStatus() != ExpenseStatus.APPROVED) {
            throw new RuntimeException("Only APPROVED expenses can be marked as paid");
        }

        expense.setStatus(ExpenseStatus.PAID);
        expenseRepository.save(expense);

        auditService.log("ExpenseRequest", expense.getId(),
            "PAID", currentUser.getEmail(), "Marked as paid");

        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setStatus(expense.getStatus());
        return response;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}