package ai.upstage.gateway.model.entity;

import ai.upstage.gateway.model.enums.OcrTransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ocr_transactions")
@Data
@NoArgsConstructor
public class OcrTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OcrTransactionStatus status;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON string

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "total_processing_time_ms")
    private Long totalProcessingTimeMs;

    @Column(name = "accident_no")
    private String accidentNo;

    @Column(name = "confirm_no")
    private String confirmNo;

    public OcrTransaction(String transactionId, OcrTransactionStatus status, String callbackUrl, String metadata,
            String accidentNo, String confirmNo) {
        this.transactionId = transactionId;
        this.status = status;
        this.callbackUrl = callbackUrl;
        this.metadata = metadata;
        this.accidentNo = accidentNo;
        this.confirmNo = confirmNo;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}