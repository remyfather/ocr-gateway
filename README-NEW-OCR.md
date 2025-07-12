# 새로운 OCR 시스템

## 개요

이 시스템은 OCR 요청을 받아서 문서 타입별로 분류하고, 각각의 전용 큐에 분배하여 처리하는 시스템입니다.

## 문서 타입

1. **진료비 영수증** (`MEDICAL_RECEIPT`)
2. **진단서** (`MEDICAL_CERTIFICATE`)
3. **입퇴원 확인서** (`ADMISSION_DISCHARGE`)
4. **수술확인서** (`SURGERY_CONFIRMATION`)

## 시스템 아키텍처

```
OCR 요청 → 문서 분류 → 큐 분배 → 컨슈머 처리
    ↓           ↓           ↓           ↓
API Gateway → Classification → RabbitMQ → OCR Processing
```

## 큐 구조

- **Exchange**: `ocr.exchange` (Topic Exchange)
- **Queues**:
  - `medical.receipt.queue` (진료비 영수증)
  - `medical.certificate.queue` (진단서)
  - `admission.discharge.queue` (입퇴원 확인서)
  - `surgery.confirmation.queue` (수술확인서)

## API 엔드포인트

### 1. OCR 요청 처리

```
POST /api/v2/ocr/process
```

**요청 예시:**

```json
{
  "accidentNumber": "ACC2024001",
  "confirmationNumber": "CONF001",
  "files": [
    {
      "fileName": "진료비영수증_001.pdf",
      "fileId": "FILE001",
      "filePath": "/path/to/file1.pdf"
    },
    {
      "fileName": "진단서_001.pdf",
      "fileId": "FILE002",
      "filePath": "/path/to/file2.pdf"
    }
  ]
}
```

**응답 예시:**

```json
{
  "status": "SUCCESS",
  "accidentNumber": "ACC2024001",
  "confirmationNumber": "CONF001",
  "totalFiles": 2,
  "classificationResults": {
    "진료비영수증": 1,
    "진단서": 1
  },
  "message": "OCR 요청이 성공적으로 큐에 분배되었습니다.",
  "timestamp": "2024-01-01T12:00:00"
}
```

### 2. 헬스체크

```
GET /api/v2/ocr/health
```

## 문서 분류 로직

파일명을 기반으로 문서 타입을 분류합니다:

- **진료비 영수증**: "진료비", "영수증", "receipt", "medical_fee" 포함
- **진단서**: "진단서", "certificate", "diagnosis" 포함
- **입퇴원 확인서**: "입퇴원", "입원", "퇴원", "admission", "discharge" 포함
- **수술확인서**: "수술", "surgery", "operation" 포함
- **기본값**: 분류되지 않는 파일은 진료비 영수증으로 분류

## 테스트

테스트 스크립트를 실행하세요:

```bash
chmod +x test-ocr-request.sh
./test-ocr-request.sh
```

## 로그 확인

각 큐의 컨슈머는 다음과 같은 로그를 출력합니다:

```
진료비 영수증 큐에서 메시지 수신 - 사고번호: ACC2024001, 확정순번: CONF001, 파일 수: 1
진료비영수증 문서 처리 시작 - 총 1개 파일
문서 처리 중 - 파일명: 진료비영수증_001.pdf, 파일ID: FILE001, 문서타입: 진료비영수증, 신뢰도: 0.9
문서 처리 완료 - 파일명: 진료비영수증_001.pdf
진료비영수증 문서 처리 완료
```

## 설정

### RabbitMQ 설정

`application.yml`에서 RabbitMQ 연결 정보를 확인하세요:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123
```

### 클러스터 모드

클러스터 모드에서는 HAProxy를 통해 연결됩니다:

```yaml
spring:
  config:
    activate:
      on-profile: cluster
  rabbitmq:
    host: localhost
    port: 5671
```

## 확장성

- 각 문서 타입별로 독립적인 큐를 사용하여 병렬 처리 가능
- 컨슈머 인스턴스를 여러 개 실행하여 처리량 향상 가능
- 새로운 문서 타입 추가 시 enum과 큐만 추가하면 됨
