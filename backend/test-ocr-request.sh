#!/bin/bash

# OCR 게이트웨이 테스트 스크립트
# 실제 존재하는 파일들로 테스트

echo "=== OCR 게이트웨이 테스트 시작 ==="

# 1. 새로운 OCR 요청 API 테스트 (실제 파일들)
echo "1. 새로운 OCR 요청 API 테스트..."

curl -X POST http://localhost:8080/api/v2/ocr/process \
  -H "Content-Type: application/json" \
  -d '{
    "id1": "2024",
    "id2": "3454943",
    "id3": "63R7933",
    "id4": "추후정의1",
    "id5": "추후정의2",
    "source": "TEST_SYSTEM",
    "requestPipeline": ["PREPROCESSING", "CLASSIFICATION", "OCR"],
    "피보상이": "Y",
    "files": [
      {
        "fileId": "FILE001",
        "fileName": "2024-3454943-63R7933-2-1_CLGB3020_1.tif",
        "filePath": "/nas/input/납입확인서/2024-3454943-63R7933-2-1_CLGB3020_1.tif",
        "fileSize": 26624,
        "mimeType": "image/tiff",
        "fileExt": "tif",
        "documentType": "납입확인서"
      },
      {
        "fileId": "FILE002",
        "fileName": "2024-3454943-63R7933-2-1_CLGB3020_2.tif",
        "filePath": "/nas/input/납입확인서/2024-3454943-63R7933-2-1_CLGB3020_2.tif",
        "fileSize": 28672,
        "mimeType": "image/tiff",
        "fileExt": "tif",
        "documentType": "납입확인서"
      },
      {
        "fileId": "FILE003",
        "fileName": "2024-5654279-6Q9202042-2-1_CLGB3009_2.jpg",
        "filePath": "/nas/input/납입확인서/2024-5654279-6Q9202042-2-1_CLGB3009_2.jpg",
        "fileSize": 226304,
        "mimeType": "image/jpeg",
        "fileExt": "jpg",
        "documentType": "납입확인서"
      }
    ]
  }'

echo -e "\n\n=== 테스트 완료 ==="
echo "로그를 확인하여 메시지 큐 처리 상태를 확인하세요." 