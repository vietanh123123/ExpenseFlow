package com.expenseapproval.dto.response;

import com.expenseapproval.enums.ExpenseCategory;
import com.expenseapproval.enums.ExpenseStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExpenseResponse {
    private Long id;
    private String title;
    private ExpenseCategory category;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String description;
    private ExpenseStatus status;
    private String submittedByName;
    private String receiptFileName;
    private LocalDateTime createdAt;
}