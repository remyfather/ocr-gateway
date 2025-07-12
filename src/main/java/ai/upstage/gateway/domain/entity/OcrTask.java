package ai.upstage.gateway.domain.entity;

import ai.upstage.gateway.domain.enums.ProcessingStageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ocr_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrTask {
    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "file_id", nullable = false)
    private String fileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProcessingStageStatus status;

    @Column(name = "current_stage")
    private String currentStage;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "temp_file_path")
    private String tempFilePath;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "retry_count")
    private int retryCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}