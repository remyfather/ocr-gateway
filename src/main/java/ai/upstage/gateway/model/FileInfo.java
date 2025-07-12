package ai.upstage.gateway.model;

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
}