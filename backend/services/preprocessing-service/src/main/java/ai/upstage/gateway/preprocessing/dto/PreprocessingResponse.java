package ai.upstage.gateway.preprocessing.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessingResponse {

    private String fileId;
    private String originalFileName;
    private String status; // SUCCESS, FAILED, PROCESSING

    private List<ProcessedImage> processedImages;
    private PreprocessingMetadata metadata;
    private String errorMessage;

    private LocalDateTime processedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessedImage {
        private String imagePath;
        private String imageName;
        private Long fileSize;
        private Integer width;
        private Integer height;
        private String format;
        private Integer dpi;
        private Integer pageNumber; // PDF의 경우
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreprocessingMetadata {
        private String originalFormat;
        private Integer originalWidth;
        private Integer originalHeight;
        private Integer originalDpi;
        private Integer pageCount; // PDF의 경우
        private Long processingTimeMs;
        private String processingSteps;
    }
}