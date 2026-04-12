package com.expenseapproval.controller;

import com.expenseapproval.dto.request.ApprovalRequest;
import com.expenseapproval.dto.response.ExpenseResponse;
import com.expenseapproval.service.ApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    // Manager approves or rejects
    @PostMapping("/{expenseId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ExpenseResponse> processApproval(
            @PathVariable Long expenseId,
            @Valid @RequestBody ApprovalRequest request) {
        return ResponseEntity.ok(approvalService.processApproval(expenseId, request));
    }

    // Finance marks as paid
    @PostMapping("/{expenseId}/pay")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<ExpenseResponse> markAsPaid(@PathVariable Long expenseId) {
        return ResponseEntity.ok(approvalService.markAsPaid(expenseId));
    }
}