# OCR Gateway Solution

OCR 게이트웨이 솔루션 - Spring Boot 백엔드와 React Admin 프론트엔드로 구성된 완전한 OCR 처리 시스템입니다.

## 🏗️ 프로젝트 구조

```
gateway/
├── frontend/          # React Admin 프론트엔드
│   ├── src/
│   │   ├── components/    # React Admin 컴포넌트
│   │   ├── App.tsx        # 메인 애플리케이션
│   │   └── dataProvider.ts # API 연결
│   ├── package.json
│   └── README.md
├── backend/           # Spring Boot 백엔드
│   ├── src/main/java/ai/upstage/gateway/
│   │   ├── GatewayApplication.java
│   │   ├── config/        # 설정
│   │   ├── controller/    # REST API
│   │   ├── service/       # 비즈니스 로직
│   │   ├── repository/    # 데이터 접근
│   │   ├── entity/        # JPA 엔티티
│   │   ├── dto/           # 데이터 전송 객체
│   │   └── messaging/     # RabbitMQ 메시징
│   ├── sql/              # 데이터베이스 스크립트
│   ├── docker-compose.yml # 개발 환경
│   ├── build.gradle
│   └── README.md
├── nas/               # 파일 저장소
│   ├── input/         # 입력 파일들
│   └── processed/     # 처리된 파일들
└── README.md          # 이 파일
```

## 🚀 빠른 시작

### 1. 백엔드 실행

```bash
cd backend
./gradlew bootRun
```

### 2. 프론트엔드 실행

```bash
cd frontend
npm install
npm start
```

### 3. 서비스 접속

- **백엔드 API**: http://localhost:8080
- **프론트엔드**: http://localhost:3000
- **RabbitMQ 관리**: http://localhost:15672 (admin/admin)

## 📋 개발 단계

### Phase 1: 기본 구조 ✅

- [x] 프로젝트 구조 재구성
- [x] Spring Boot 백엔드 기본 설정
- [x] React Admin 프론트엔드 구성
- [x] 불필요한 파일 정리

### Phase 2: 데이터베이스 설계

- [ ] 엔티티 설계 (OCR 작업, 트랜잭션, 처리 단계)
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

### Backend

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA
- **Message Queue**: RabbitMQ
- **Build Tool**: Gradle

### Frontend

- **Framework**: React Admin
- **Language**: TypeScript
- **UI Library**: Material-UI
- **Build Tool**: npm/yarn

## 📝 API 엔드포인트

개발 진행에 따라 API 문서가 업데이트됩니다.

## 🐳 Docker 지원

```bash
# 전체 시스템 실행
cd backend
docker-compose up -d

# 백엔드만 실행
docker-compose up backend

# 프론트엔드만 실행
docker-compose up frontend
```

## 📚 문서

- [백엔드 문서](./backend/README.md)
- [프론트엔드 문서](./frontend/README.md)
- [RabbitMQ 아키텍처](./backend/rabbitmq-architecture-diagram.md)
- [큐 구조](./backend/rabbitmq-queue-structure.md)
- [수정된 아키텍처](./backend/corrected-architecture.md)
