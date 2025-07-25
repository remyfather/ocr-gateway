package ai.upstage.gateway.preprocessing.controller;

import ai.upstage.gateway.preprocessing.dto.PreprocessingRequest;
import ai.upstage.gateway.preprocessing.dto.PreprocessingResponse;
import ai.upstage.gateway.preprocessing.service.ImagePreprocessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/preprocessing")
@RequiredArgsConstructor
public class PreprocessingController {

    private final ImagePreprocessingService imagePreprocessingService;

    /**
     * 이미지 파일 전처리 API
     */
    @PostMapping("/image")
    public ResponseEntity<PreprocessingResponse> preprocessImage(
            @Valid @RequestBody PreprocessingRequest request) {

        log.info("이미지 전처리 요청 - 파일ID: {}, 파일명: {}", request.getFileId(), request.getFileName());

        PreprocessingResponse response = imagePreprocessingService.preprocessImage(
                request.getFilePath(),
                request.getFileId(),
                request.getFileName());

        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 전처리 서비스 상태 확인
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Preprocessing Service is running");
    }

    /**
     * 전처리 설정 정보 조회
     */
    @GetMapping("/config")
    public ResponseEntity<String> getConfig() {
        return ResponseEntity.ok("Preprocessing configuration info");
    }
}