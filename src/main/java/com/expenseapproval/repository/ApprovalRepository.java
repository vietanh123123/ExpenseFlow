package com.expenseapproval.repository;

import com.expenseapproval.entity.ApprovalAction;
import com.expenseapproval.entity.ExpenseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<ApprovalAction, Long> {
    List<ApprovalAction> findByExpenseRequest(ExpenseRequest expenseRequest);
}