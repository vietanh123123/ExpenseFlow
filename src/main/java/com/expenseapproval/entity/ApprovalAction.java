package com.expenseapproval.entity;

import com.expenseapproval.enums.ExpenseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_actions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ApprovalAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_request_id", nullable = false)
    private ExpenseRequest expenseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acted_by_id", nullable = false)
    private User actedBy;

    // What action was taken: APPROVED, REJECTED, PAID
    @Enumerated(EnumType.STRING)
    private ExpenseStatus action;

    private String comment;

    @Column(updatable = false)
    private LocalDateTime actionAt;

    @PrePersist
    protected void onCreate() {
        actionAt = LocalDateTime.now();
    }
}