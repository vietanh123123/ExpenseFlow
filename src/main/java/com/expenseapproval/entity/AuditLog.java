package com.expenseapproval.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "ExpenseRequest", "User"
    private String entityName;

    private Long entityId;

    // e.g. "STATUS_CHANGED", "CREATED", "APPROVED"
    private String action;

    private String performedBy;

    private String details;

    @Column(updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}