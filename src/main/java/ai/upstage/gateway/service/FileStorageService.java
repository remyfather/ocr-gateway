package ai.upstage.gateway.service;

import ai.upstage.gateway.config.FileStorageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 파일 저장소 관리 서비스
 * NAS에서 파일을 가져와서 게이트웨이에서 처리하는 구조
 */
@Slf4j
@Service
public class FileStorageService {

    @Autowired
    private FileStorageConfig fileStorageConfig;

    /**
     * 파일 존재 여부 확인
     */
    public boolean fileExists(String filePath) {
        if (!fileStorageConfig.isValidFilePath(filePath)) {
            return false;
        }

        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * 파일 크기 확인
     */
    public long getFileSize(String filePath) {
        if (!fileExists(filePath)) {
            return -1;
        }

        try {
            Path path = Paths.get(filePath);
            return Files.size(path);
        } catch (IOException e) {
            log.error("Failed to get file size for: {}", filePath, e);
            return -1;
        }
    }

    /**
     * NAS에서 게이트웨이 임시 처리 디렉토리로 파일 복사
     */
    public String copyFromNasToTemp(String nasFilePath) {
        if (!fileStorageConfig.isNasPath(nasFilePath)) {
            log.error("Source file is not a NAS path: {}", nasFilePath);
            return null;
        }

        if (!fileExists(nasFilePath)) {
            log.error("NAS file does not exist: {}", nasFilePath);
            return null;
        }

        try {
            // 임시 처리 파일명 생성
            String tempFileName = "processing_" + UUID.randomUUID().toString() + "_" +
                    Paths.get(nasFilePath).getFileName().toString();
            Path tempPath = Paths.get(fileStorageConfig.getTempProcessingDirectory(), tempFileName);

            // 디렉토리가 없으면 생성
            Files.createDirectories(tempPath.getParent());

            // 파일 복사
            Files.copy(Paths.get(nasFilePath), tempPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File copied from NAS to temp processing: {} -> {}", nasFilePath, tempPath);
            return tempPath.toString();

        } catch (IOException e) {
            log.error("Failed to copy file from NAS to temp processing: {}", nasFilePath, e);
            return null;
        }
    }

    /**
     * 처리 완료된 파일을 NAS 처리 완료 디렉토리로 이동
     */
    public String moveToNasProcessed(String tempFilePath, String transactionId) {
        if (!fileStorageConfig.isTempPath(tempFilePath)) {
            log.error("Source file is not a temp path: {}", tempFilePath);
            return null;
        }

        if (!fileExists(tempFilePath)) {
            log.error("Temp file does not exist: {}", tempFilePath);
            return null;
        }

        try {
            // NAS 처리 완료 파일명 생성
            String processedFileName = "processed_" + transactionId + "_" +
                    Paths.get(tempFilePath).getFileName().toString();
            Path nasProcessedPath = Paths.get(fileStorageConfig.getNasProcessedDirectory(), processedFileName);

            // 디렉토리가 없으면 생성
            Files.createDirectories(nasProcessedPath.getParent());

            // 파일 이동
            Files.move(Paths.get(tempFilePath), nasProcessedPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File moved to NAS processed: {} -> {}", tempFilePath, nasProcessedPath);
            return nasProcessedPath.toString();

        } catch (IOException e) {
            log.error("Failed to move file to NAS processed: {}", tempFilePath, e);
            return null;
        }
    }

    /**
     * 임시 처리 파일 정리
     */
    public boolean cleanupTempProcessingFile(String tempFilePath) {
        if (tempFilePath == null || !tempFilePath.contains("processing_")) {
            return false;
        }

        try {
            Path path = Paths.get(tempFilePath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("Temp processing file cleaned up: {}", tempFilePath);
                return true;
            }
        } catch (IOException e) {
            log.error("Failed to cleanup temp processing file: {}", tempFilePath, e);
        }
        return false;
    }

    /**
     * 임시 결과 파일 정리
     */
    public boolean cleanupTempResultFile(String tempFilePath) {
        if (tempFilePath == null || !tempFilePath.contains("result_")) {
            return false;
        }

        try {
            Path path = Paths.get(tempFilePath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("Temp result file cleaned up: {}", tempFilePath);
                return true;
            }
        } catch (IOException e) {
            log.error("Failed to cleanup temp result file: {}", tempFilePath, e);
        }
        return false;
    }

    /**
     * 파일 유효성 검사
     */
    public boolean validateFile(String filePath) {
        if (!fileStorageConfig.isValidFilePath(filePath)) {
            log.warn("Invalid file path: {}", filePath);
            return false;
        }

        if (!fileExists(filePath)) {
            log.warn("File does not exist: {}", filePath);
            return false;
        }

        String fileName = Paths.get(filePath).getFileName().toString();
        if (!fileStorageConfig.isAllowedExtension(fileName)) {
            log.warn("File extension not allowed: {}", fileName);
            return false;
        }

        long fileSize = getFileSize(filePath);
        if (fileSize > fileStorageConfig.getMaxFileSize() * 1024 * 1024) {
            log.warn("File size too large: {} bytes", fileSize);
            return false;
        }

        return true;
    }

    /**
     * NAS 파일 경로 생성 (테스트용)
     */
    public String createNasFilePath(String fileName) {
        return Paths.get(fileStorageConfig.getNasInputDirectory(), fileName).toString();
    }

    /**
     * 임시 결과 파일 경로 생성
     */
    public String createTempResultPath(String fileName, String transactionId) {
        String resultFileName = "result_" + transactionId + "_" + fileName;
        return Paths.get(fileStorageConfig.getTempResultDirectory(), resultFileName).toString();
    }
}