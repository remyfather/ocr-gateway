package ai.upstage.gateway.domain.enums;

public enum ProcessingStageType {
    PREPROCESSING, // 전처리
    CLASSIFICATION, // 문서 분류
    EXTRACTION, // 추출 (1주차 추가)
    TEXT_EXTRACTION, // 텍스트 추출
    TABLE_EXTRACTION, // 테이블 추출
    FORM_EXTRACTION, // 폼 추출
    SIGNATURE_EXTRACTION, // 서명 추출
    POSTPROCESSING, // 후처리
    CALLBACK // 콜백
}