package ai.upstage.gateway.api.controller;

import ai.upstage.gateway.api.dto.request.OcrRequestDto;
import ai.upstage.gateway.processing.service.NewOcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2/ocr")
@RequiredArgsConstructor
@Slf4j
public class NewOcrController {

    private final NewOcrService newOcrService;

    /**
     * OCR 요청 처리 엔드포인트
     * 
     * 요청 예시:
     * {
     * "accidentNumber": "ACC2024001",
     * "confirmationNumber": "CONF001",
     * "files": [
     * {
     * "fileName": "진료비영수증_001.pdf",
     * "fileId": "FILE001",
     * "filePath": "/path/to/file1.pdf"
     * },
     * {
     * "fileName": "진단서_001.pdf",
     * "fileId": "FILE002",
     * "filePath": "/path/to/file2.pdf"
     * }
     * ]
     * }
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processOcrRequest(@RequestBody OcrRequestDto ocrRequest) {
        log.info("새로운 OCR 요청 수신 - 사고번호: {}, 확정순번: {}",
                ocrRequest.getAccidentNumber(), ocrRequest.getConfirmationNumber());

        try {
            Map<String, Object> result = newOcrService.processOcrRequest(ocrRequest);

            if ("SUCCESS".equals(result.get("status"))) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (Exception e) {
            log.error("OCR 요청 처리 중 예외 발생", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "ERROR",
                    "message", "서버 내부 오류: " + e.getMessage()));
        }
    }

    /**
     * 헬스체크 엔드포인트
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "New OCR Gateway",
                "timestamp", java.time.LocalDateTime.now()));
    }

    /**
     * 큐 상태 확인 엔드포인트
     */
    @GetMapping("/queues/status")
    public ResponseEntity<Map<String, Object>> getQueueStatus() {
        try {
            Map<String, Object> queueStatus = new HashMap<>();

            // 각 문서 타입별 큐 상태
            queueStatus.put("medical_receipt_queue", Map.of(
                    "name", "medical.receipt.queue",
                    "status", "active",
                    "description", "진료비 영수증 처리 큐"));

            queueStatus.put("medical_certificate_queue", Map.of(
                    "name", "medical.certificate.queue",
                    "status", "active",
                    "description", "진단서 처리 큐"));

            queueStatus.put("admission_discharge_queue", Map.of(
                    "name", "admission.discharge.queue",
                    "status", "active",
                    "description", "입퇴원 확인서 처리 큐"));

            queueStatus.put("surgery_confirmation_queue", Map.of(
                    "name", "surgery.confirmation.queue",
                    "status", "active",
                    "description", "수술확인서 처리 큐"));

            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "queues", queueStatus,
                    "total_queues", 4,
                    "timestamp", java.time.LocalDateTime.now()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "ERROR",
                    "message", "큐 상태 확인 실패: " + e.getMessage()));
        }
    }
}