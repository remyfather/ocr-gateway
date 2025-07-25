#!/bin/bash

# 파일 전처리 서비스 테스트 스크립트

echo "=== 파일 전처리 서비스 테스트 ==="

# 1. 서비스 상태 확인
echo "1. 서비스 상태 확인..."
curl -X GET http://localhost:8081/api/v1/preprocessing/health
echo -e "\n"

# 2. 이미지 전처리 테스트
echo "2. 이미지 전처리 테스트..."
curl -X POST http://localhost:8081/api/v1/preprocessing/image \
  -H "Content-Type: application/json" \
  -d '{
    "filePath": "/Users/yongho/Desktop/upstage/02. 메리츠화재(AIOCR)/gateway/nas/input/납입확인서/2024-4977499-6AAST-31000-2-1_CLGB3020_1.tif",
    "fileName": "2024-4977499-6AAST-31000-2-1_CLGB3020_1.tif",
    "fileId": "test-file-001",
    "fileSize": 25908,
    "mimeType": "image/tiff",
    "documentType": "납입확인서",
    "targetDpi": 150,
    "jpegQuality": 0.95,
    "outputFormat": "jpeg",
    "maxWidth": 2000,
    "maxHeight": 2000
  }'
echo -e "\n"

# 3. 설정 정보 조회
echo "3. 설정 정보 조회..."
curl -X GET http://localhost:8081/api/v1/preprocessing/config
echo -e "\n"

echo "=== 테스트 완료 ===" 