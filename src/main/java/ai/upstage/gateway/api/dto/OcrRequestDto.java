package ai.upstage.gateway.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrRequestDto {
    private String accidentNo; // 사고번호
    private String confirmNo; // 확정번호
    private String fileId; // 파일ID
    private String imageUrl; // 기존
    private String callbackUrl; // 기존
}