package ai.upstage.gateway.repository;

import ai.upstage.gateway.model.entity.ProcessingStageEntity;
import ai.upstage.gateway.model.enums.ProcessingStageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingStageRepository extends CrudRepository<ProcessingStageEntity, Long> {

        List<ProcessingStageEntity> findByTransactionId(String transactionId);

        ProcessingStageEntity findByTransactionIdAndStageType(String transactionId, ProcessingStageType stageType);

        List<ProcessingStageEntity> findByTransactionIdOrderByCreatedAtAsc(String transactionId);

        List<ProcessingStageEntity> findByStatus(String status);
}