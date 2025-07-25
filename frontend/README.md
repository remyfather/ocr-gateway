# OCR Gateway 관리도구 (React Admin)

OCR Gateway 백엔드의 관리도구입니다. React Admin을 기반으로 구축되었습니다.

## 기능

- **대시보드**: OCR 작업 통계 및 현황
- **OCR 작업 관리**: 작업 목록, 상세보기, 편집
- **OCR 거래 관리**: 거래 목록 및 상세보기
- **처리 단계 관리**: 각 작업의 처리 단계 모니터링

## 설치 및 실행

### 1. 의존성 설치

```bash
npm install
```

### 2. 개발 서버 실행

```bash
npm start
```

개발 서버는 `http://localhost:3000`에서 실행됩니다.

### 3. 프로덕션 빌드

```bash
npm run build
```

## 환경 설정

### 백엔드 API 연결

- 기본적으로 `http://localhost:8080`의 Spring Boot 백엔드에 연결됩니다.
- `package.json`의 `proxy` 설정을 통해 CORS 문제를 해결합니다.

### 환경 변수

- `REACT_APP_API_URL`: 백엔드 API URL (기본값: http://localhost:8080)

## 프로젝트 구조

```
frontend/
├── src/
│   ├── components/           # React Admin 컴포넌트들
│   │   ├── Dashboard.tsx     # 대시보드
│   │   ├── Layout.tsx        # 레이아웃
│   │   ├── OcrTask.tsx       # OCR 작업 관리
│   │   ├── OcrTransaction.tsx # OCR 거래 관리
│   │   └── ProcessingStage.tsx # 처리 단계 관리
│   ├── dataProvider.ts       # API 데이터 프로바이더
│   ├── App.tsx              # 메인 앱 컴포넌트
│   └── index.tsx            # 앱 진입점
├── package.json
└── README.md
```

## API 엔드포인트

관리도구는 다음 API 엔드포인트들을 사용합니다:

- `GET /api/ocr/tasks` - OCR 작업 목록
- `GET /api/ocr/tasks/:id` - OCR 작업 상세
- `PUT /api/ocr/tasks/:id` - OCR 작업 수정
- `GET /api/ocr/transactions` - OCR 거래 목록
- `GET /api/ocr/transactions/:id` - OCR 거래 상세
- `GET /api/ocr/processing-stages` - 처리 단계 목록
- `GET /api/ocr/processing-stages/:id` - 처리 단계 상세
- `GET /api/ocr/statistics` - 통계 데이터

## 개발 가이드

### 새로운 리소스 추가

1. `src/components/` 디렉토리에 새 컴포넌트 생성
2. `src/App.tsx`에 Resource 추가
3. `src/dataProvider.ts`에 API 엔드포인트 추가

### 스타일링

- Material-UI 컴포넌트 사용
- `sx` prop을 통한 인라인 스타일링
- 필요시 커스텀 테마 적용 가능

## 문제 해결

### CORS 에러

- 백엔드에서 CORS 설정 확인
- `package.json`의 proxy 설정 확인

### API 연결 실패

- 백엔드 서버가 실행 중인지 확인
- API 엔드포인트 URL 확인
- 네트워크 탭에서 요청/응답 확인
