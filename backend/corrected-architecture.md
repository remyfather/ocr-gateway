# 올바른 역할 분리 아키텍처

## 🎯 문제점 인식

현재 Gateway가 Producer와 Consumer 역할을 모두 담당하고 있어 **역할 분리가 명확하지 않음**

## ✅ 올바른 구조

### 1. **Gateway (API Gateway) - Producer 전용**

```
┌─────────────────────────────────────────────────────────────────┐
│                        Gateway Service                          │
│                    (Producer 역할만)                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │   Controller    │───▶│   Service       │───▶│   Producer  │ │
│  │   (API)         │    │   (Business)    │    │   (Queue)   │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│                                                                 │
│  • API 엔드포인트 제공                                           │
│  • 요청 검증 및 문서 분류                                        │
│  • 큐에 메시지 전송 (Producer)                                   │
│  • 응답 반환                                                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 2. **OCR Worker Services - Consumer 전용**

```
┌─────────────────────────────────────────────────────────────────┐
│                    OCR Worker Services                          │
│                   (Consumer 역할만)                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │ Medical Receipt │    │ Medical Cert.   │    │ Admission   │ │
│  │   Worker        │    │   Worker        │    │   Worker    │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│           │                       │                      │      │
│           ▼                       ▼                      ▼      │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │   Consumer      │    │   Consumer      │    │   Consumer  │ │
│  │   (Queue)       │    │   (Queue)       │    │   (Queue)   │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│                                                                 │
│  • 큐에서 메시지 수신 (Consumer)                                 │
│  • OCR 처리 로직 실행                                            │
│  • 결과 저장 및 처리                                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 전체 아키텍처 흐름

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client        │───▶│   Gateway       │───▶│   RabbitMQ      │
│   (Upload)      │    │   (Producer)    │    │   (Exchange/    │
└─────────────────┘    └─────────────────┘    │   Queue)        │
                                              └─────────┬───────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
                                              │   OCR Workers   │
                                              │   (Consumers)   │
                                              └─────────────────┘
```

## 📋 역할별 책임 명확화

### **Gateway Service (Producer)**

```java
✅ API 엔드포인트 제공
✅ 요청 검증
✅ 문서 분류
✅ 큐에 메시지 전송
✅ 응답 반환
❌ 큐에서 메시지 수신 (제거 필요)
❌ OCR 처리 (제거 필요)
```

### **OCR Worker Services (Consumers)**

```java
✅ 큐에서 메시지 수신
✅ OCR 처리 로직
✅ 결과 저장
✅ 에러 처리
❌ API 엔드포인트 제공 (제거 필요)
❌ 문서 분류 (제거 필요)
```

## 🚀 개선 방안

### 1. **Gateway에서 Consumer 제거**

```java
// 제거해야 할 부분
@RabbitListener(queues = RabbitMQConfig.MEDICAL_RECEIPT_QUEUE)
public void consumeMedicalReceipt(OcrQueueMessage message) {
    // 이 부분을 별도 OCR Worker로 이동
}
```

### 2. **별도 OCR Worker 서비스 생성**

```java
// 새로운 OCR Worker 서비스
@Service
public class MedicalReceiptOcrWorker {

    @RabbitListener(queues = RabbitMQConfig.MEDICAL_RECEIPT_QUEUE)
    public void processMedicalReceipt(OcrQueueMessage message) {
        // OCR 처리 로직
    }
}
```

### 3. **마이크로서비스 분리**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Gateway       │    │   OCR Worker    │    │   OCR Worker    │
│   Service       │    │   Service 1     │    │   Service 2     │
│   (Port 8080)   │    │   (Port 8081)   │    │   (Port 8082)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🎯 최종 권장 구조

### **1단계: 역할 분리**

- Gateway에서 Consumer 코드 제거
- Producer 역할만 유지

### **2단계: 서비스 분리**

- OCR Worker를 별도 서비스로 분리
- 각 문서 타입별 독립적인 Worker

### **3단계: 마이크로서비스화**

- 각 서비스를 독립적으로 배포
- 서비스 간 통신은 RabbitMQ만 사용

이렇게 하면 **단일 책임 원칙**을 지키고 **확장성**과 **유지보수성**을 크게 향상시킬 수 있습니다! 🎉
