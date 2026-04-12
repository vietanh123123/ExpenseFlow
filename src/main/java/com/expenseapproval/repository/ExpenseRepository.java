package com.expenseapproval.repository;

import com.expenseapproval.entity.ExpenseRequest;
import com.expenseapproval.entity.User;
import com.expenseapproval.enums.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseRequest, Long> {

    // All expenses submitted by a specific user
    List<ExpenseRequest> findBySubmittedBy(User user);

    // All expenses with a specific status (e.g. all PENDING for manager)
    List<ExpenseRequest> findByStatus(ExpenseStatus status);

    // Expenses by user and status
    List<ExpenseRequest> findBySubmittedByAndStatus(User user, ExpenseStatus status);

    // Total spending this month for dashboard
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseRequest e " +
           "WHERE e.status = 'APPROVED' " +
           "AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE) " +
           "AND YEAR(e.expenseDate) = YEAR(CURRENT_DATE)")
    BigDecimal getTotalApprovedThisMonth();

    // Count by status
    long countByStatus(ExpenseStatus status);
}