package kz.iitu.springlab.tx;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    private final AuditRepository repo;

    public AuditService(AuditRepository repo) { this.repo = repo; }

    @Transactional(propagation = Propagation.REQUIRED)
    public void logRequired(String message) { repo.save(new AuditLog(message)); }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logRequiresNew(String message) { repo.save(new AuditLog(message)); }
}