package ai.upstage.gateway.domain.enums;

public enum ProcessingStageStatus {
    PENDING, // 대기 중
    PROCESSING, // 처리 중 (1주차 추가)
    IN_PROGRESS, // 처리 중 (기존)
    COMPLETED, // 완료
    FAILED, // 실패
    RETRY, // 재시도
    CANCELLED // 취소
}