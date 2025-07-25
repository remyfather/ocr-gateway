# 파일 전처리 서비스 (Preprocessing Service)

## 개요

이미지 파일의 전처리를 담당하는 마이크로서비스입니다.

## 주요 기능

- 이미지 리사이징 (DPI 150, JPEG 품질 95%)
- 이미지 회전 보정
- 다중 페이지 처리 (PDF)
- 이미지 포맷 변환

## 기술 스택

- Spring Boot 3.2.0
- Java 17
- TwelveMonkeys ImageIO (이미지 처리)
- Apache PDFBox (PDF 처리)
- Lombok

## 실행 방법

### 1. 서비스 빌드

```bash
cd services/preprocessing-service
./gradlew build
```

### 2. 서비스 실행

```bash
./gradlew bootRun
```

### 3. 서비스 확인

```bash
curl http://localhost:8081/api/v1/preprocessing/health
```

## API 엔드포인트

### 이미지 전처리

```
POST /api/v1/preprocessing/image
Content-Type: application/json

{
  "filePath": "/nas/input/sample_image.jpg",
  "fileName": "sample_image.jpg",
  "fileId": "test-file-001",
  "fileSize": 1024000,
  "mimeType": "image/jpeg",
  "documentType": "receipt",
  "targetDpi": 150,
  "jpegQuality": 0.95,
  "outputFormat": "jpeg",
  "maxWidth": 2000,
  "maxHeight": 2000
}
```

### 서비스 상태 확인

```
GET /api/v1/preprocessing/health
```

### 설정 정보 조회

```
GET /api/v1/preprocessing/config
```

## 설정

### application.yml

```yaml
server:
  port: 8081

preprocessing:
  image:
    target-dpi: 150
    jpeg-quality: 0.95
    output-format: jpeg
    max-width: 2000
    max-height: 2000
  paths:
    input-base: /nas/input
    output-base: /nas/processed
    temp-base: /temp/processing
```

## 테스트

```bash
# 테스트 스크립트 실행
./test-preprocessing.sh

# 또는 개별 테스트
curl -X POST http://localhost:8081/api/v1/preprocessing/image \
  -H "Content-Type: application/json" \
  -d '{
    "filePath": "/nas/input/sample_image.jpg",
    "fileName": "sample_image.jpg",
    "fileId": "test-file-001",
    "fileSize": 1024000
  }'
```

## 출력 파일

전처리된 이미지는 다음 경로에 저장됩니다:

```
/nas/processed/{fileId}/{fileName}_processed.{format}
```

## 로그

- 로그 레벨: DEBUG
- 로그 패턴: `yyyy-MM-dd HH:mm:ss - %msg%n`
