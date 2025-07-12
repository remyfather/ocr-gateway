package ai.upstage.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractionResult {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("document_type")
    private String documentType;

    @JsonProperty("extraction_type")
    private String extractionType; // TEXT, TABLE, FORM, SIGNATURE

    @JsonProperty("extracted_data")
    private ExtractedData extractedData;

    @JsonProperty("confidence_scores")
    private Map<String, Double> confidenceScores;

    @JsonProperty("processing_metadata")
    private ProcessingMetadata processingMetadata;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractedData {
        @JsonProperty("text_content")
        private String textContent;

        @JsonProperty("tables")
        private List<TableData> tables;

        @JsonProperty("forms")
        private List<FormField> forms;

        @JsonProperty("signatures")
        private List<SignatureData> signatures;

        @JsonProperty("structured_data")
        private Map<String, Object> structuredData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TableData {
        @JsonProperty("table_id")
        private String tableId;

        @JsonProperty("rows")
        private List<List<String>> rows;

        @JsonProperty("headers")
        private List<String> headers;

        @JsonProperty("confidence")
        private Double confidence;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormField {
        @JsonProperty("field_name")
        private String fieldName;

        @JsonProperty("field_value")
        private String fieldValue;

        @JsonProperty("field_type")
        private String fieldType;

        @JsonProperty("confidence")
        private Double confidence;

        @JsonProperty("coordinates")
        private Coordinates coordinates;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignatureData {
        @JsonProperty("signature_id")
        private String signatureId;

        @JsonProperty("signature_type")
        private String signatureType;

        @JsonProperty("confidence")
        private Double confidence;

        @JsonProperty("coordinates")
        private Coordinates coordinates;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates {
        @JsonProperty("x")
        private Double x;

        @JsonProperty("y")
        private Double y;

        @JsonProperty("width")
        private Double width;

        @JsonProperty("height")
        private Double height;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingMetadata {
        @JsonProperty("processing_time_ms")
        private Long processingTimeMs;

        @JsonProperty("model_version")
        private String modelVersion;

        @JsonProperty("extraction_quality_score")
        private Double extractionQualityScore;
    }
}