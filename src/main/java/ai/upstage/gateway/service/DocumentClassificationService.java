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
     */
    public DocumentClassificationResult classifyDocument(FileInfo fileInfo) {
        String fileName = fileInfo.getFileName().toLowerCase();

        // 파일명 패턴을 기반으로 문서 타입 분류
        if (containsAny(fileName, "진료비", "영수증", "receipt", "medical_fee")) {
            return new DocumentClassificationResult(fileInfo, DocumentType.MEDICAL_RECEIPT, 0.9);
        } else if (containsAny(fileName, "진단서", "certificate", "diagnosis")) {
            return new DocumentClassificationResult(fileInfo, DocumentType.MEDICAL_CERTIFICATE, 0.9);
        } else if (containsAny(fileName, "입퇴원", "입원", "퇴원", "admission", "discharge")) {
            return new DocumentClassificationResult(fileInfo, DocumentType.ADMISSION_DISCHARGE, 0.9);
        } else if (containsAny(fileName, "수술", "surgery", "operation")) {
            return new DocumentClassificationResult(fileInfo, DocumentType.SURGERY_CONFIRMATION, 0.9);
        } else {
            // 기본값으로 진료비 영수증으로 분류
            return new DocumentClassificationResult(fileInfo, DocumentType.MEDICAL_RECEIPT, 0.5);
        }
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}