package com.expenseapproval.dto.request;

import com.expenseapproval.enums.ExpenseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalRequest {
    @NotNull
    private ExpenseStatus action; // APPROVED or REJECTED

    private String comment;
}