package ai.upstage.gateway.domain.enums;

public enum DocumentType {
    MEDICAL_RECEIPT("진료비영수증"),
    MEDICAL_CERTIFICATE("진단서"),
    ADMISSION_DISCHARGE("입퇴원확인서"),
    SURGERY_CONFIRMATION("수술확인서");

    private final String description;

    DocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}