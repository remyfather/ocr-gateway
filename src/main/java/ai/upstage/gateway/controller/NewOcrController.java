package ai.upstage.gateway.controller;

import ai.upstage.gateway.model.OcrRequestDto;
import ai.upstage.gateway.service.NewOcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
}