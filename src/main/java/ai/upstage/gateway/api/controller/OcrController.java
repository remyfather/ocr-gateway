package ai.upstage.gateway.api.controller;

import ai.upstage.gateway.api.dto.OcrProcessRequest;
import ai.upstage.gateway.api.dto.OcrTaskMessage;
import ai.upstage.gateway.processing.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
@Slf4j
public class OcrController {

    @Autowired
    private OcrService ocrService;

    /**
     * OCR 처리 요청 API (새로운 인터페이스)
     * 사고번호, 확정순번, 파일 리스트를 받아서 처리
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processOcrRequest(@RequestBody OcrProcessRequest request) {
        log.info("Received OCR processing request for accident: {}, confirm: {}",
                request.getAccidentNo(), request.getConfirmNo());

        try {
            // 1. 요청 유효성 검사
            if (request.getAccidentNo() == null || request.getAccidentNo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "ERROR",
                        "message", "사고번호(accidentNo)는 필수입니다.",
                        "timestamp", java.time.LocalDateTime.now()));
            }

            if (request.getConfirmNo() == null || request.getConfirmNo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "ERROR",
                        "message", "확정순번(confirmNo)는 필수입니다.",
                        "timestamp", java.time.LocalDateTime.now()));
            }

            if (request.getFileList() == null || request.getFileList().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "ERROR",
                        "message", "파일 리스트(fileList)는 필수입니다.",
                        "timestamp", java.time.LocalDateTime.now()));
            }

            // 2. OCR 처리 요청
            Map<String, Object> response = ocrService.processOcrRequest(request);

            if ("ERROR".equals(response.get("status"))) {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing OCR request for accident: {}, confirm: {}",
                    request.getAccidentNo(), request.getConfirmNo(), e);

            Map<String, Object> errorResponse = Map.of(
                    "status", "ERROR",
                    "message", "Internal server error: " + e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 트랜잭션 상태 조회 API
     */
    @GetMapping("/status/{transactionId}")
    public ResponseEntity<Map<String, Object>> getTransactionStatus(@PathVariable String transactionId) {
        log.info("Getting status for transaction: {}", transactionId);

        try {
            Map<String, Object> response = ocrService.getTransactionStatus(transactionId);

            if ("NOT_FOUND".equals(response.get("status"))) {
                return ResponseEntity.notFound().build();
            }

            if ("ERROR".equals(response.get("status"))) {
                return ResponseEntity.internalServerError().body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting status for transaction: {}", transactionId, e);

            Map<String, Object> errorResponse = Map.of(
                    "transactionId", transactionId,
                    "status", "ERROR",
                    "message", "Internal server error: " + e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 태스크 상태 조회 API
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Map<String, Object>> getTaskStatus(@PathVariable String taskId) {
        log.info("Getting task status for task: {}", taskId);

        try {
            Map<String, Object> response = ocrService.getTaskStatus(taskId);

            if ("NOT_FOUND".equals(response.get("status"))) {
                return ResponseEntity.notFound().build();
            }

            if ("ERROR".equals(response.get("status"))) {
                return ResponseEntity.internalServerError().body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting task status for task: {}", taskId, e);

            Map<String, Object> errorResponse = Map.of(
                    "taskId", taskId,
                    "status", "ERROR",
                    "message", "Internal server error: " + e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 사고번호별 처리 상태 조회 API
     */
    @GetMapping("/accident/{accidentNo}")
    public ResponseEntity<Map<String, Object>> getAccidentStatus(@PathVariable String accidentNo) {
        log.info("Getting status for accident: {}", accidentNo);

        try {
            Map<String, Object> response = ocrService.getAccidentStatus(accidentNo);

            if ("NOT_FOUND".equals(response.get("status"))) {
                return ResponseEntity.notFound().build();
            }

            if ("ERROR".equals(response.get("status"))) {
                return ResponseEntity.internalServerError().body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting status for accident: {}", accidentNo, e);

            Map<String, Object> errorResponse = Map.of(
                    "accidentNo", accidentNo,
                    "status", "ERROR",
                    "message", "Internal server error: " + e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 헬스체크 API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = Map.of(
                "status", "UP",
                "service", "OCR Gateway",
                "version", "1.0.0",
                "timestamp", java.time.LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}