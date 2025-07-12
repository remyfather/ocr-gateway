package ai.upstage.gateway.domain.repository;

import ai.upstage.gateway.domain.entity.OcrTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OcrTaskRepository extends JpaRepository<OcrTask, String> {
    Optional<OcrTask> findByTaskId(String taskId);

    List<OcrTask> findByTransactionId(String transactionId);
}