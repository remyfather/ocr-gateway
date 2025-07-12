package ai.upstage.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OCR 처리 요청 DTO (실제 업무 요구사항 기반)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrProcessRequest {

    @JsonProperty("accidentNo")
    private String accidentNo; // 사고번호 (INDEX02)

    @JsonProperty("confirmNo")
    private String confirmNo; // 확정순번 (INDEX03)

    @JsonProperty("fileList")
    private List<FileInfo> fileList; // 파일 정보 리스트

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        @JsonProperty("fileId")
        private String fileId; // 파일 ID

        @JsonProperty("filePath")
        private String filePath; // 파일 경로 (로컬 테스트용)

        @JsonProperty("fileName")
        private String fileName; // 파일명

        @JsonProperty("fileSize")
        private Long fileSize; // 파일 크기

        @JsonProperty("mimeType")
        private String mimeType; // MIME 타입

        @JsonProperty("documentType")
        private String documentType; // 문서 타입 (INVOICE, RECEIPT, etc.)
    }
}