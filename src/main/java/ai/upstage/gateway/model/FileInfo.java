package ai.upstage.gateway.model;

import ai.upstage.gateway.model.enums.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private String fileName; // 파일명 (필수)
    private String fileId; // 파일ID (필수)
    private String filePath; // 파일 경로
    private DocumentType documentType; // 문서 타입 (필수)
}