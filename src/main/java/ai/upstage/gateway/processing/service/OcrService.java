package ai.upstage.gateway.processing.service;

import ai.upstage.gateway.api.dto.OcrProcessRequest;
import ai.upstage.gateway.api.dto.OcrTaskMessage;
import ai.upstage.gateway.domain.entity.OcrTask;
import ai.upstage.gateway.domain.entity.OcrTransaction;
import ai.upstage.gateway.domain.enums.OcrTransactionStatus;
import ai.upstage.gateway.domain.enums.ProcessingStageStatus;
import ai.upstage.gateway.domain.enums.ProcessingStageType;
import ai.upstage.gateway.domain.repository.OcrTaskRepository;
import ai.upstage.gateway.domain.repository.OcrTransactionRepository;
import ai.upstage.gateway.domain.repository.ProcessingStageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OcrService {

    @Autowired
    private OcrTransactionRepository ocrTransactionRepository;

    @Autowired
    private OcrTaskRepository ocrTaskRepository;

    @Autowired
    private ProcessingStageRepository processingStageRepository;

    @Autowired
    private ai.upstage.gateway.storage.service.FileStorageService fileStorageService;

    /**
     * OCR 처리 요청 처리 (새로운 인터페이스)
     */
    @Transactional
    public Map<String, Object> processOcrRequest(OcrProcessRequest request) {
        log.info("Processing OCR request for accident: {}, confirm: {}, files: {}",
                request.getAccidentNo(), request.getConfirmNo(), request.getFileList().size());

        try {
            // 1. 파일 유효성 검사 (NAS 경로 확인)
            List<String> invalidFiles = new ArrayList<>();
            for (OcrProcessRequest.FileInfo fileInfo : request.getFileList()) {
                if (!fileStorageService.validateFile(fileInfo.getFilePath())) {
                    invalidFiles.add(fileInfo.getFileId());
                }
            }

            if (!invalidFiles.isEmpty()) {
                return Map.of(
                        "status", "ERROR",
                        "message", "Invalid files found: " + String.join(", ", invalidFiles),
                        "timestamp", LocalDateTime.now());
            }

            // 2. 트랜잭션 생성
            String transactionId = generateTransactionId();
            OcrTransaction transaction = createTransaction(transactionId, request);

            // 3. 각 파일별로 NAS에서 임시 디렉토리로 복사 후 태스크 생성
            List<String> taskIds = new ArrayList<>();
            for (OcrProcessRequest.FileInfo fileInfo : request.getFileList()) {
                // NAS에서 게이트웨이 임시 디렉토리로 파일 복사
                String tempFilePath = fileStorageService.copyFromNasToTemp(fileInfo.getFilePath());
                if (tempFilePath == null) {
                    log.error("Failed to copy file from NAS: {}", fileInfo.getFilePath());
                    continue;
                }

                String taskId = generateTaskId();
                OcrTask task = createTask(taskId, transactionId, fileInfo, tempFilePath);
                createProcessingStages(transactionId, taskId);

                // 표준화된 메시지로 변환 (임시 경로 사용)
                OcrTaskMessage taskMessage = convertToTaskMessage(fileInfo, taskId, transactionId, request,
                        tempFilePath);

                // 전처리 큐로 메시지 발행 (기존 시스템 비활성화)
                // ocrRequestProducer.sendToPreprocessingQueue(taskMessage);

                taskIds.add(taskId);
                log.info("Created task {} for file: {} (temp: {})", taskId, fileInfo.getFileId(), tempFilePath);
            }

            log.info("Successfully queued {} tasks for preprocessing", taskIds.size());

            return Map.of(
                    "status", "SUCCESS",
                    "transactionId", transactionId,
                    "taskIds", taskIds,
                    "accidentNo", request.getAccidentNo(),
                    "confirmNo", request.getConfirmNo(),
                    "fileCount", request.getFileList().size(),
                    "message", "OCR processing request accepted and queued for preprocessing",
                    "timestamp", LocalDateTime.now());

        } catch (Exception e) {
            log.error("Error processing OCR request for accident: {}, confirm: {}",
                    request.getAccidentNo(), request.getConfirmNo(), e);
            return Map.of(
                    "status", "ERROR",
                    "message", "Failed to process OCR request: " + e.getMessage(),
                    "timestamp", LocalDateTime.now());
        }
    }

    /**
     * 트랜잭션 상태 조회
     */
    public Map<String, Object> getTransactionStatus(String transactionId) {
        log.info("Getting status for transaction: {}", transactionId);

        try {
            Optional<OcrTransaction> transactionOpt = ocrTransactionRepository.findByTransactionId(transactionId);

            if (transactionOpt.isEmpty()) {
                return Map.of(
                        "status", "NOT_FOUND",
                        "message", "Transaction not found: " + transactionId,
                        "timestamp", LocalDateTime.now());
            }

            OcrTransaction transaction = transactionOpt.get();

            return Map.of(
                    "transactionId", transactionId,
                    "status", transaction.getStatus().name(),
                    "accidentNo", transaction.getAccidentNo(),
                    "confirmNo", transaction.getConfirmNo(),
                    "createdAt", transaction.getCreatedAt(),
                    "updatedAt", transaction.getUpdatedAt(),
                    "message", "Transaction status retrieved successfully",
                    "timestamp", LocalDateTime.now());

        } catch (Exception e) {
            log.error("Error getting status for transaction: {}", transactionId, e);
            return Map.of(
                    "status", "ERROR",
                    "message", "Failed to get transaction status: " + e.getMessage(),
                    "timestamp", LocalDateTime.now());
        }
    }

    /**
     * 태스크 상태 조회
     */
    public Map<String, Object> getTaskStatus(String taskId) {
        log.info("Getting task status for task: {}", taskId);

        try {
            Optional<OcrTask> taskOpt = ocrTaskRepository.findByTaskId(taskId);

            if (taskOpt.isEmpty()) {
                return Map.of(
                        "status", "NOT_FOUND",
                        "message", "Task not found: " + taskId,
                        "timestamp", LocalDateTime.now());
            }

            OcrTask task = taskOpt.get();

            return Map.of(
                    "taskId", taskId,
                    "transactionId", task.getTransactionId(),
                    "status", task.getStatus().name(),
                    "stage", task.getCurrentStage(),
                    "fileId", task.getFileId(),
                    "createdAt", task.getCreatedAt(),
                    "updatedAt", task.getUpdatedAt(),
                    "message", "Task status retrieved successfully",
                    "timestamp", LocalDateTime.now());

        } catch (Exception e) {
            log.error("Error getting task status for task: {}", taskId, e);
            return Map.of(
                    "status", "ERROR",
                    "message", "Failed to get task status: " + e.getMessage(),
                    "timestamp", LocalDateTime.now());
        }
    }

    /**
     * 사고번호별 처리 상태 조회
     */
    public Map<String, Object> getAccidentStatus(String accidentNo) {
        log.info("Getting status for accident: {}", accidentNo);

        try {
            List<OcrTransaction> transactions = ocrTransactionRepository.findByAccidentNo(accidentNo);

            if (transactions.isEmpty()) {
                return Map.of(
                        "status", "NOT_FOUND",
                        "message", "No transactions found for accident: " + accidentNo,
                        "timestamp", LocalDateTime.now());
            }

            // 각 트랜잭션별 태스크 상태 집계
            Map<String, Object> transactionStatuses = new HashMap<>();
            for (OcrTransaction transaction : transactions) {
                List<OcrTask> tasks = ocrTaskRepository.findByTransactionId(transaction.getTransactionId());

                Map<String, Object> taskStatuses = tasks.stream()
                        .collect(Collectors.toMap(
                                OcrTask::getTaskId,
                                task -> Map.of(
                                        "status", task.getStatus().name(),
                                        "stage", task.getCurrentStage(),
                                        "fileId", task.getFileId())));

                transactionStatuses.put(transaction.getTransactionId(), Map.of(
                        "status", transaction.getStatus().name(),
                        "confirmNo", transaction.getConfirmNo(),
                        "createdAt", transaction.getCreatedAt(),
                        "tasks", taskStatuses));
            }

            return Map.of(
                    "accidentNo", accidentNo,
                    "status", "SUCCESS",
                    "transactionCount", transactions.size(),
                    "transactions", transactionStatuses,
                    "message", "Accident status retrieved successfully",
                    "timestamp", LocalDateTime.now());

        } catch (Exception e) {
            log.error("Error getting status for accident: {}", accidentNo, e);
            return Map.of(
                    "status", "ERROR",
                    "message", "Failed to get accident status: " + e.getMessage(),
                    "timestamp", LocalDateTime.now());
        }
    }

    /**
     * 트랜잭션 생성
     */
    private OcrTransaction createTransaction(String transactionId, OcrProcessRequest request) {
        // 생성자 사용으로 코드 단순화
        OcrTransaction transaction = new OcrTransaction(
                transactionId,
                OcrTransactionStatus.PENDING,
                null, // callbackUrl (필요시 request에서 세팅)
                null, // metadata (필요시 request에서 세팅)
                request.getAccidentNo(),
                request.getConfirmNo());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return ocrTransactionRepository.save(transaction);
    }

    /**
     * 태스크 생성
     */
    private OcrTask createTask(String taskId, String transactionId, OcrProcessRequest.FileInfo fileInfo,
            String tempFilePath) {
        OcrTask task = new OcrTask();
        task.setTaskId(taskId);
        task.setTransactionId(transactionId);
        task.setFileId(fileInfo.getFileId());
        task.setStatus(ProcessingStageStatus.PENDING);
        task.setCurrentStage(ProcessingStageType.PREPROCESSING.name());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setTempFilePath(tempFilePath);

        return ocrTaskRepository.save(task);
    }

    /**
     * 처리 단계 초기화
     */
    private void createProcessingStages(String transactionId, String taskId) {
        ProcessingStageType[] stages = {
                ProcessingStageType.PREPROCESSING,
                ProcessingStageType.CLASSIFICATION,
                ProcessingStageType.EXTRACTION,
                ProcessingStageType.POSTPROCESSING
        };

        for (ProcessingStageType stage : stages) {
            var processingStage = new ai.upstage.gateway.domain.entity.ProcessingStageEntity();
            processingStage.setTransactionId(transactionId);
            processingStage.setTaskId(taskId);
            processingStage.setStageType(stage);
            processingStage.setStatus(ProcessingStageStatus.PENDING);
            processingStage.setCreatedAt(LocalDateTime.now());
            processingStage.setUpdatedAt(LocalDateTime.now());

            processingStageRepository.save(processingStage);
        }
    }

    /**
     * FileInfo를 OcrTaskMessage로 변환
     */
    private OcrTaskMessage convertToTaskMessage(OcrProcessRequest.FileInfo fileInfo, String taskId,
            String transactionId, OcrProcessRequest request, String tempFilePath) {
        OcrTaskMessage taskMessage = new OcrTaskMessage();
        taskMessage.setTaskId(taskId);
        taskMessage.setTransactionId(transactionId);
        taskMessage.setFileId(fileInfo.getFileId());
        taskMessage.setFileUrl(tempFilePath);
        taskMessage.setFileName(fileInfo.getFileName());
        taskMessage.setFileSize(fileInfo.getFileSize());
        taskMessage.setMimeType(fileInfo.getMimeType());
        taskMessage.setDocumentType(ai.upstage.gateway.domain.enums.DocumentType.valueOf(fileInfo.getDocumentType()));
        taskMessage.setCreatedAt(LocalDateTime.now());
        taskMessage.setStage("PREPROCESSING");

        // Metadata 설정
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("accidentNo", request.getAccidentNo());
        metadata.put("confirmNo", request.getConfirmNo());
        metadata.put("filePath", fileInfo.getFilePath());
        taskMessage.setMetadata(metadata);

        // 기본 ExtractionOptions 설정
        OcrTaskMessage.ExtractionOptions options = new OcrTaskMessage.ExtractionOptions();
        options.setExtractText(true);
        options.setExtractTables(false);
        options.setExtractForms(false);
        options.setExtractSignatures(false);
        options.setLanguage("ko");
        options.setConfidenceThreshold(0.8);
        taskMessage.setExtractionOptions(options);

        return taskMessage;
    }

    /**
     * 트랜잭션 ID 생성
     */
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 태스크 ID 생성
     */
    private String generateTaskId() {
        return "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}