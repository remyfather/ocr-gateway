package ai.upstage.gateway.preprocessing.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessingRequest {

    @NotBlank(message = "파일 경로는 필수입니다")
    private String filePath;

    @NotBlank(message = "파일명은 필수입니다")
    private String fileName;

    @NotBlank(message = "파일 ID는 필수입니다")
    private String fileId;

    @NotNull(message = "파일 크기는 필수입니다")
    private Long fileSize;

    private String mimeType;
    private String documentType;

    // 전처리 옵션
    private Integer targetDpi = 150;
    private Float jpegQuality = 0.95f;
    private String outputFormat = "jpeg";
    private Integer maxWidth = 2000;
    private Integer maxHeight = 2000;
}