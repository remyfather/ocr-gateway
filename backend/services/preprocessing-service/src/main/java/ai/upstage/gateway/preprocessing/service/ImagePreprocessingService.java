package ai.upstage.gateway.preprocessing.service;

import ai.upstage.gateway.preprocessing.dto.PreprocessingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImagePreprocessingService {

    @Value("${preprocessing.image.target-dpi:150}")
    private int targetDpi;

    @Value("${preprocessing.image.jpeg-quality:0.95}")
    private float jpegQuality;

    @Value("${preprocessing.image.output-format:jpeg}")
    private String outputFormat;

    @Value("${preprocessing.image.max-width:2000}")
    private int maxWidth;

    @Value("${preprocessing.image.max-height:2000}")
    private int maxHeight;

    @Value("${preprocessing.paths.output-base:/nas/processed}")
    private String outputBasePath;

    @Value("${preprocessing.paths.temp-base:/temp/processing}")
    private String tempBasePath;

    /**
     * 이미지 파일 전처리
     */
    public PreprocessingResponse preprocessImage(String filePath, String fileId, String fileName) {
        log.info("이미지 전처리 시작 - 파일: {}, 파일ID: {}", filePath, fileId);

        long startTime = System.currentTimeMillis();

        try {
            // 파일 존재 확인
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                throw new IOException("파일이 존재하지 않습니다: " + filePath);
            }

            // 이미지 읽기
            BufferedImage originalImage = ImageIO.read(inputFile);
            if (originalImage == null) {
                throw new IOException("이미지를 읽을 수 없습니다: " + filePath);
            }

            // 이미지 전처리
            BufferedImage processedImage = processImage(originalImage);

            // 출력 디렉토리 생성
            String outputDir = createOutputDirectory(fileId);
            String outputFileName = generateOutputFileName(fileName, fileId);
            String outputPath = Paths.get(outputDir, outputFileName).toString();

            // 이미지 저장
            saveImage(processedImage, outputPath);

            // 메타데이터 수집
            PreprocessingResponse.PreprocessingMetadata metadata = createMetadata(
                    originalImage, processedImage, System.currentTimeMillis() - startTime);

            // 처리된 이미지 정보
            List<PreprocessingResponse.ProcessedImage> processedImages = new ArrayList<>();
            processedImages.add(createProcessedImageInfo(outputPath, outputFileName, processedImage));

            log.info("이미지 전처리 완료 - 파일ID: {}, 출력경로: {}", fileId, outputPath);

            return new PreprocessingResponse(
                    fileId, fileName, "SUCCESS", processedImages, metadata, null,
                    java.time.LocalDateTime.now());

        } catch (Exception e) {
            log.error("이미지 전처리 실패 - 파일ID: {}, 오류: {}", fileId, e.getMessage(), e);

            return new PreprocessingResponse(
                    fileId, fileName, "FAILED", null, null, e.getMessage(),
                    java.time.LocalDateTime.now());
        }
    }

    /**
     * 이미지 처리 (리사이징, DPI 조정)
     */
    private BufferedImage processImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 리사이징 계산
        double scaleFactor = calculateScaleFactor(originalWidth, originalHeight);
        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        // 이미지 리사이징
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 렌더링 품질 설정
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * 스케일 팩터 계산
     */
    private double calculateScaleFactor(int width, int height) {
        double scaleX = (double) maxWidth / width;
        double scaleY = (double) maxHeight / height;
        return Math.min(1.0, Math.min(scaleX, scaleY));
    }

    /**
     * 출력 디렉토리 생성
     */
    private String createOutputDirectory(String fileId) throws IOException {
        String outputDir = Paths.get(outputBasePath, fileId).toString();
        Files.createDirectories(Paths.get(outputDir));
        return outputDir;
    }

    /**
     * 출력 파일명 생성
     */
    private String generateOutputFileName(String originalFileName, String fileId) {
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        return baseName + "_processed." + outputFormat;
    }

    /**
     * 이미지 저장
     */
    private void saveImage(BufferedImage image, String outputPath) throws IOException {
        File outputFile = new File(outputPath);

        // 기본 ImageIO 사용 (JPEG 품질 설정은 나중에 구현)
        ImageIO.write(image, outputFormat, outputFile);
    }

    /**
     * 메타데이터 생성
     */
    private PreprocessingResponse.PreprocessingMetadata createMetadata(
            BufferedImage originalImage, BufferedImage processedImage, long processingTime) {
        return new PreprocessingResponse.PreprocessingMetadata(
                "image", originalImage.getWidth(), originalImage.getHeight(),
                targetDpi, 1, processingTime, "resize,quality_adjustment");
    }

    /**
     * 처리된 이미지 정보 생성
     */
    private PreprocessingResponse.ProcessedImage createProcessedImageInfo(
            String imagePath, String imageName, BufferedImage image) {
        File imageFile = new File(imagePath);
        return new PreprocessingResponse.ProcessedImage(
                imagePath, imageName, imageFile.length(),
                image.getWidth(), image.getHeight(), outputFormat, targetDpi, 1);
    }
}