package com.expenseapproval.service;

import com.expenseapproval.dto.request.ExpenseSubmitRequest;
import com.expenseapproval.dto.response.ExpenseResponse;
import com.expenseapproval.entity.ExpenseRequest;
import com.expenseapproval.entity.User;
import com.expenseapproval.enums.ExpenseStatus;
import com.expenseapproval.exception.ResourceNotFoundException;
import com.expenseapproval.repository.ExpenseRepository;
import com.expenseapproval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final AuditService auditService;

    public ExpenseResponse submitExpense(ExpenseSubmitRequest request, MultipartFile file) {
        User currentUser = getCurrentUser();

        // Handle file upload
        String fileName = null;
        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = fileStorageService.storeFile(file);
            fileName = file.getOriginalFilename();
        }

        ExpenseRequest expense = ExpenseRequest.builder()
            .title(request.getTitle())
            .category(request.getCategory())
            .amount(request.getAmount())
            .expenseDate(request.getExpenseDate())
            .description(request.getDescription())
            .receiptFilePath(filePath)
            .receiptFileName(fileName)
            .submittedBy(currentUser)
            .build();

        expenseRepository.save(expense);

        auditService.log("ExpenseRequest", expense.getId(),
            "CREATED", currentUser.getEmail(), "Expense submitted");

        return mapToResponse(expense);
    }

    public List<ExpenseResponse> getMyExpenses() {
        User currentUser = getCurrentUser();
        return expenseRepository.findBySubmittedBy(currentUser)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ExpenseResponse> getPendingExpenses() {
        return expenseRepository.findByStatus(ExpenseStatus.PENDING)
            .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
            .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(Long id) {
        ExpenseRequest expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return mapToResponse(expense);
    }

    // Helper: get currently logged-in user from Spring Security context
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Helper: convert entity to DTO
    private ExpenseResponse mapToResponse(ExpenseRequest expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setCategory(expense.getCategory());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setDescription(expense.getDescription());
        response.setStatus(expense.getStatus());
        response.setSubmittedByName(expense.getSubmittedBy().getFullName());
        response.setReceiptFileName(expense.getReceiptFileName());
        response.setCreatedAt(expense.getCreatedAt());
        return response;
    }
}