package com.expenseapproval.dto.request;

import com.expenseapproval.enums.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseSubmitRequest {
    @NotBlank
    private String title;

    @NotNull
    private ExpenseCategory category;

    @NotNull @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate expenseDate;

    private String description;
    // File is handled separately as MultipartFile in the controller
}