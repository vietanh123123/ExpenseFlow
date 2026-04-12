package com.expenseapproval.entity;

import com.expenseapproval.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String department;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "submittedBy", fetch = FetchType.LAZY)
    private List<ExpenseRequest> expenses;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}