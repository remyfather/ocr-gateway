package ai.upstage.gateway.model;

import ai.upstage.gateway.model.enums.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentClassificationResult {
    private FileInfo fileInfo;
    private DocumentType documentType;
    private double confidence; // 분류 신뢰도
}