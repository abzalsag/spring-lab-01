package kz.iitu.springlab.tx;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OuterService {
    private final AuditService audit;

    public OuterService(AuditService audit) { this.audit = audit; }

    @Transactional
    public void outerWithRequired(boolean fail) {
        audit.logRequired("audit via REQUIRED");
        if (fail) throw new IllegalStateException("Outer failed");
    }

    @Transactional
    public void outerWithRequiresNew(boolean fail) {
        audit.logRequiresNew("audit via REQUIRES_NEW");
        if (fail) throw new IllegalStateException("Outer failed");
    }
}