package ai.upstage.gateway.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 파일 저장소 설정
 * NAS: 원본 파일 저장소 (임시 NAS 시뮬레이션)
 * Temp: 게이트웨이 서버의 임시 처리 디렉토리
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageConfig {

    /**
     * NAS 입력 디렉토리 (원본 파일들이 저장되는 곳 - 임시 NAS)
     */
    private String nasInputDirectory = "nas/input";

    /**
     * NAS 처리 완료 디렉토리 (처리 완료된 파일들이 저장되는 곳 - 임시 NAS)
     */
    private String nasProcessedDirectory = "nas/processed";

    /**
     * 게이트웨이 임시 처리 디렉토리 (처리 중인 파일들이 저장되는 곳)
     */
    private String tempProcessingDirectory = "temp/processing";

    /**
     * 게이트웨이 임시 결과 디렉토리 (처리 결과가 저장되는 곳)
     */
    private String tempResultDirectory = "temp/result";

    /**
     * 최대 파일 크기 (MB)
     */
    private long maxFileSize = 50;

    /**
     * 허용된 파일 확장자
     */
    private String[] allowedExtensions = { ".pdf", ".jpg", ".jpeg", ".png", ".tiff", ".bmp" };

    /**
     * 파일 경로가 유효한지 확인
     */
    public boolean isValidFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        // 보안상 상위 디렉토리 접근 방지
        if (filePath.contains("..") || filePath.startsWith("/")) {
            return false;
        }

        return true;
    }

    /**
     * 파일 확장자가 허용되는지 확인
     */
    public boolean isAllowedExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        String lowerFileName = fileName.toLowerCase();
        for (String extension : allowedExtensions) {
            if (lowerFileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * NAS 경로인지 확인
     */
    public boolean isNasPath(String filePath) {
        return filePath != null
                && (filePath.startsWith(nasInputDirectory) || filePath.startsWith(nasProcessedDirectory));
    }

    /**
     * 임시 경로인지 확인
     */
    public boolean isTempPath(String filePath) {
        return filePath != null
                && (filePath.startsWith(tempProcessingDirectory) || filePath.startsWith(tempResultDirectory));
    }
}