package ai.upstage.gateway.repository;

import ai.upstage.gateway.model.entity.OcrTransaction;
import ai.upstage.gateway.model.enums.OcrTransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OcrTransactionRepository extends JpaRepository<OcrTransaction, Long> {

        Optional<OcrTransaction> findByTransactionId(String transactionId);

        List<OcrTransaction> findByStatus(OcrTransactionStatus status);

        List<OcrTransaction> findByAccidentNo(String accidentNo);

        List<OcrTransaction> findByAccidentNoAndConfirmNo(String accidentNo, String confirmNo);

        List<OcrTransaction> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}