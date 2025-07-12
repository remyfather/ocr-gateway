#!/bin/bash

# 새로운 OCR 시스템 테스트 스크립트

echo "=== 새로운 OCR 시스템 테스트 ==="

# 서버 헬스체크
echo "1. 서버 헬스체크..."
curl -X GET http://localhost:8080/api/v2/ocr/health
echo -e "\n"

# 큐 상태 확인
echo "2. 큐 상태 확인..."
curl -X GET http://localhost:8080/api/v2/ocr/queues/status
echo -e "\n"

# OCR 요청 테스트
echo "3. OCR 요청 테스트..."
curl -X POST http://localhost:8080/api/v2/ocr/process \
  -H "Content-Type: application/json" \
  -d '{
    "accidentNumber": "ACC2024001",
    "confirmationNumber": "CONF001",
    "files": [
      {
        "fileName": "진료비영수증_001.pdf",
        "fileId": "FILE001",
        "filePath": "/nas/medical/receipt_001.pdf",
        "documentType": "MEDICAL_RECEIPT"
      },
      {
        "fileName": "진단서_001.pdf",
        "fileId": "FILE002",
        "filePath": "/nas/medical/certificate_001.pdf",
        "documentType": "MEDICAL_CERTIFICATE"
      },
      {
        "fileName": "입퇴원확인서_001.pdf",
        "fileId": "FILE003",
        "filePath": "/nas/medical/admission_001.pdf",
        "documentType": "ADMISSION_DISCHARGE"
      },
      {
        "fileName": "수술확인서_001.pdf",
        "fileId": "FILE004",
        "filePath": "/nas/medical/surgery_001.pdf",
        "documentType": "SURGERY_CONFIRMATION"
      },
      {
        "fileName": "기타문서_001.pdf",
        "fileId": "FILE005",
        "filePath": "/nas/medical/other_001.pdf",
        "documentType": "MEDICAL_RECEIPT"
      }
    ]
  }'
echo -e "\n"

echo "=== 테스트 완료 ===" 