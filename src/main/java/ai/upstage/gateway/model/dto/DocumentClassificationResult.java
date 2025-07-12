package ai.upstage.gateway.model.dto;

import ai.upstage.gateway.model.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentClassificationResult {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("detected_document_type")
    private DocumentType detectedDocumentType;

    @JsonProperty("confidence_score")
    private Double confidenceScore;

    @JsonProperty("alternative_types")
    private List<DocumentType> alternativeTypes;

    @JsonProperty("processing_metadata")
    private ProcessingMetadata processingMetadata;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingMetadata {
        @JsonProperty("page_count")
        private Integer pageCount;

        @JsonProperty("image_quality_score")
        private Double imageQualityScore;

        @JsonProperty("orientation")
        private String orientation;

        @JsonProperty("language_detected")
        private String languageDetected;

        @JsonProperty("processing_time_ms")
        private Long processingTimeMs;
    }
}