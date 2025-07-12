package ai.upstage.gateway.processing.service;

import ai.upstage.gateway.messaging.producer.OcrMessageProducer;
import ai.upstage.gateway.processing.model.DocumentClassificationResult;
import ai.upstage.gateway.api.dto.request.OcrRequestDto;
import ai.upstage.gateway.api.dto.request.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewOcrService {

    private final DocumentClassificationService documentClassificationService;
    private final OcrMessageProducer ocrMessageProducer;

    /**
     * OCR 요청을 받아서 문서 타입별로 분류하고 큐에 분배합니다.
     */
    public Map<String, Object> processOcrRequest(OcrRequestDto ocrRequest) {
        log.info("새로운 OCR 요청 처리 시작 - 사고번호: {}, 확정순번: {}, 파일 수: {}",
                ocrRequest.getAccidentNumber(), ocrRequest.getConfirmationNumber(), ocrRequest.getFiles().size());

        try {
            // 1. 입력 유효성 검사
            validateRequest(ocrRequest);

            // 2. 문서 타입 분류
            List<DocumentClassificationResult> classifications = documentClassificationService
                    .classifyDocuments(ocrRequest.getFiles());

            log.info("문서 분류 완료 - 총 {}개 파일 분류됨", classifications.size());

            // 3. 문서 타입별로 큐에 분배
            ocrMessageProducer.distributeOcrRequest(ocrRequest, classifications);

            // 4. 분류 결과 요약
            Map<String, Long> typeCounts = classifications.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            result -> result.getDetectedDocumentType().getDescription(),
                            java.util.stream.Collectors.counting()));

            log.info("큐 분배 완료 - 분류 결과: {}", typeCounts);

            return Map.of(
                    "status", "SUCCESS",
                    "accidentNumber", ocrRequest.getAccidentNumber(),
                    "confirmationNumber", ocrRequest.getConfirmationNumber(),
                    "totalFiles", ocrRequest.getFiles().size(),
                    "classificationResults", typeCounts,
                    "message", "OCR 요청이 성공적으로 큐에 분배되었습니다.",
                    "timestamp", LocalDateTime.now());

        } catch (Exception e) {
            log.error("OCR 요청 처리 중 오류 발생 - 사고번호: {}, 확정순번: {}",
                    ocrRequest.getAccidentNumber(), ocrRequest.getConfirmationNumber(), e);

            return Map.of(
                    "status", "ERROR",
                    "accidentNumber", ocrRequest.getAccidentNumber(),
                    "confirmationNumber", ocrRequest.getConfirmationNumber(),
                    "message", "OCR 요청 처리 실패: " + e.getMessage(),
                    "timestamp", LocalDateTime.now());
        }
    }

    /**
     * 요청 유효성 검사
     */
    private void validateRequest(OcrRequestDto ocrRequest) {
        if (ocrRequest.getAccidentNumber() == null || ocrRequest.getAccidentNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("사고번호는 필수입니다.");
        }

        if (ocrRequest.getConfirmationNumber() == null || ocrRequest.getConfirmationNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("확정순번은 필수입니다.");
        }

        if (ocrRequest.getFiles() == null || ocrRequest.getFiles().isEmpty()) {
            throw new IllegalArgumentException("파일 리스트는 필수입니다.");
        }

        // 각 파일의 필수 필드 검사
        for (int i = 0; i < ocrRequest.getFiles().size(); i++) {
            FileInfo file = ocrRequest.getFiles().get(i);
            if (file.getFileName() == null || file.getFileName().trim().isEmpty()) {
                throw new IllegalArgumentException("파일명은 필수입니다. (인덱스: " + i + ")");
            }
            if (file.getFileId() == null || file.getFileId().trim().isEmpty()) {
                throw new IllegalArgumentException("파일ID는 필수입니다. (인덱스: " + i + ")");
            }
            if (file.getDocumentType() == null) {
                throw new IllegalArgumentException("문서 타입은 필수입니다. (인덱스: " + i + ")");
            }
        }
    }
}