# OCR Gateway Backend

Spring Boot 기반 OCR 게이트웨이 백엔드 애플리케이션입니다.

## 🏗️ 아키텍처

```
backend/
├── src/main/java/ai/upstage/gateway/
│   ├── GatewayApplication.java      # 메인 애플리케이션
│   ├── config/                      # 설정 클래스들
│   ├── controller/                  # REST API 컨트롤러
│   ├── service/                     # 비즈니스 로직 서비스
│   ├── repository/                  # 데이터 접근 계층
│   ├── entity/                      # JPA 엔티티
│   ├── dto/                         # 데이터 전송 객체
│   ├── messaging/                   # RabbitMQ 메시징
│   └── exception/                   # 예외 처리
├── src/main/resources/
│   └── application.yml              # 애플리케이션 설정
├── sql/                             # 데이터베이스 스크립트
├── docker-compose.yml               # 개발 환경 Docker 설정
├── docker-compose-cluster.yml       # 클러스터 환경 Docker 설정
├── test-ocr-request.sh              # API 테스트 스크립트
├── rabbitmq-architecture-diagram.md # RabbitMQ 아키텍처 문서
├── rabbitmq-queue-structure.md      # 큐 구조 문서
├── corrected-architecture.md        # 수정된 아키텍처 문서
└── README.md                        # 이 파일
```

## 🚀 실행 방법

### 1. 의존성 설치

```bash
./gradlew build
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 3. 테스트 실행

```bash
./gradlew test
```

### 4. Docker로 실행

```bash
# 개발 환경
docker-compose up -d

# 클러스터 환경
docker-compose -f docker-compose-cluster.yml up -d
```

### 5. API 테스트

```bash
chmod +x test-ocr-request.sh
./test-ocr-request.sh
```

## 📋 개발 단계

### Phase 1: 기본 구조 설정 ✅

- [x] Spring Boot 프로젝트 생성
- [x] 기본 설정 파일 구성
- [x] Gradle 빌드 설정
- [x] 필요한 파일들 이동 및 정리

### Phase 2: 데이터베이스 설계

- [ ] 엔티티 설계
- [ ] Repository 구현
- [ ] 데이터베이스 스키마 생성

### Phase 3: API 설계

- [ ] REST API 컨트롤러 구현
- [ ] DTO 클래스 정의
- [ ] 서비스 레이어 구현

### Phase 4: 메시징 시스템

- [ ] RabbitMQ 설정
- [ ] 메시지 프로듀서/컨슈머 구현
- [ ] 큐 구조 설계

### Phase 5: OCR 처리 파이프라인

- [ ] 파일 업로드 처리
- [ ] 이미지 전처리
- [ ] 문서 분류
- [ ] OCR 결과 처리

## 🔧 기술 스택

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA
- **Message Queue**: RabbitMQ
- **Build Tool**: Gradle
- **Testing**: JUnit 5

## 📝 API 문서

개발 진행에 따라 API 문서가 업데이트됩니다.

## 📚 관련 문서

- [RabbitMQ 아키텍처](./rabbitmq-architecture-diagram.md)
- [큐 구조](./rabbitmq-queue-structure.md)
- [수정된 아키텍처](./corrected-architecture.md)
