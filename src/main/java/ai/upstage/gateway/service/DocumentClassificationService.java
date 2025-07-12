package ai.upstage.gateway.service;

import ai.upstage.gateway.model.DocumentClassificationResult;
import ai.upstage.gateway.model.FileInfo;
import ai.upstage.gateway.model.enums.DocumentType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentClassificationService {

    /**
     * 파일 리스트를 받아서 각 파일의 문서 타입을 분류합니다.
     */
    public List<DocumentClassificationResult> classifyDocuments(List<FileInfo> files) {
        return files.stream()
                .map(this::classifyDocument)
                .collect(Collectors.toList());
    }

    /**
     * 단일 파일의 문서 타입을 분류합니다.
     * 이제 파일에 직접 지정된 documentType을 사용합니다.
     */
    public DocumentClassificationResult classifyDocument(FileInfo fileInfo) {
        // 파일에 직접 지정된 documentType 사용
        DocumentType documentType = fileInfo.getDocumentType();

        if (documentType == null) {
            // documentType이 지정되지 않은 경우 기본값 사용
            return new DocumentClassificationResult(fileInfo, DocumentType.MEDICAL_RECEIPT, 0.5);
        }

        // 지정된 documentType 사용 (신뢰도 1.0)
        return new DocumentClassificationResult(fileInfo, documentType, 1.0);
    }
}