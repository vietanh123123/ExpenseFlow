package com.expenseapproval.entity;

import com.expenseapproval.enums.ExpenseCategory;
import com.expenseapproval.enums.ExpenseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "expense_requests")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ExpenseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private LocalDate expenseDate;

    private String description;

    // Path to the uploaded file on disk
    private String receiptFilePath;

    private String receiptFileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_id", nullable = false)
    private User submittedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "expenseRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApprovalAction> approvalActions;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = ExpenseStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}