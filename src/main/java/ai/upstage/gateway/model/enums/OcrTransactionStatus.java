package ai.upstage.gateway.model.enums;

public enum OcrTransactionStatus {
    PENDING("대기중"),
    IN_PROGRESS("처리중"),
    COMPLETED("완료"),
    FAILED("실패");

    private final String description;

    OcrTransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}