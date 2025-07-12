package ai.upstage.gateway.model.entity;

import ai.upstage.gateway.model.enums.ProcessingStageType;
import ai.upstage.gateway.model.enums.ProcessingStageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "processing_stages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingStageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "task_id", nullable = false)
    private String taskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", nullable = false)
    private ProcessingStageType stageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProcessingStageStatus status;

    @Column(name = "queue_name")
    private String queueName;

    @Column(name = "worker_id")
    private String workerId;

    @Column(name = "input_data", columnDefinition = "TEXT")
    private String inputData; // JSON string

    @Column(name = "output_data", columnDefinition = "TEXT")
    private String outputData; // JSON string

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "max_retries")
    private Integer maxRetries = 3;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "next_stage_type")
    @Enumerated(EnumType.STRING)
    private ProcessingStageType nextStageType;

    @Column(name = "priority")
    private Integer priority = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ProcessingStageEntity(String transactionId, ProcessingStageType stageType, ProcessingStageStatus status,
            String queueName, String inputData, ProcessingStageType nextStageType) {
        this.transactionId = transactionId;
        this.stageType = stageType;
        this.status = status;
        this.queueName = queueName;
        this.inputData = inputData;
        this.nextStageType = nextStageType;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.startedAt != null && this.completedAt != null) {
            this.processingTimeMs = java.time.Duration.between(this.startedAt, this.completedAt).toMillis();
        }
    }
}