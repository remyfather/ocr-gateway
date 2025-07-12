package ai.upstage.gateway.api.dto;

import ai.upstage.gateway.domain.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * OCR 태스크 메시지 표준 스키마
 * 모든 워커 간 통신에 사용되는 표준 메시지 형식
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrTaskMessage {

    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_size")
    private Long fileSize;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("document_type")
    private DocumentType documentType;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("extraction_options")
    private ExtractionOptions extractionOptions;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("stage")
    private String stage; // PREPROCESSING, CLASSIFICATION, EXTRACTION, POSTPROCESSING

    @JsonProperty("priority")
    private Integer priority = 0;

    @JsonProperty("retry_count")
    private Integer retryCount = 0;

    @JsonProperty("max_retries")
    private Integer maxRetries = 3;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractionOptions {
        @JsonProperty("extract_text")
        private boolean extractText = true;

        @JsonProperty("extract_tables")
        private boolean extractTables = false;

        @JsonProperty("extract_forms")
        private boolean extractForms = false;

        @JsonProperty("extract_signatures")
        private boolean extractSignatures = false;

        @JsonProperty("language")
        private String language = "ko";

        @JsonProperty("confidence_threshold")
        private Double confidenceThreshold = 0.8;

        @JsonProperty("page_range")
        private String pageRange; // "1-5" or "all"

        @JsonProperty("ocr_model")
        private String ocrModel = "default";
    }
}