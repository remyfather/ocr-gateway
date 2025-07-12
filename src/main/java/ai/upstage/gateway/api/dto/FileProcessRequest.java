package ai.upstage.gateway.api.dto;

import ai.upstage.gateway.domain.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileProcessRequest {

    @JsonProperty("fileId")
    private String fileId;

    @JsonProperty("fileUrl")
    private String fileUrl;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("fileSize")
    private Long fileSize;

    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("documentType")
    private DocumentType documentType;

    @JsonProperty("callbackUrl")
    private String callbackUrl;

    @JsonProperty("extractionOptions")
    private ExtractionOptions extractionOptions;

    @JsonProperty("metadata")
    private Object metadata;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractionOptions {
        @JsonProperty("extractText")
        private boolean extractText = true;

        @JsonProperty("extractTables")
        private boolean extractTables = false;

        @JsonProperty("extractForms")
        private boolean extractForms = false;

        @JsonProperty("extractSignatures")
        private boolean extractSignatures = false;

        @JsonProperty("language")
        private String language = "ko";

        @JsonProperty("confidenceThreshold")
        private Double confidenceThreshold = 0.8;
    }
}