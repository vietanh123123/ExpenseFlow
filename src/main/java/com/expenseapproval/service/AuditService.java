package com.expenseapproval.service;

import com.expenseapproval.entity.AuditLog;
import com.expenseapproval.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String entityName, Long entityId, String action,
                    String performedBy, String details) {
        AuditLog log = AuditLog.builder()
            .entityName(entityName)
            .entityId(entityId)
            .action(action)
            .performedBy(performedBy)
            .details(details)
            .build();
        auditLogRepository.save(log);
    }
}