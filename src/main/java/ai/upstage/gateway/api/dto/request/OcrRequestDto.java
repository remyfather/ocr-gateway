package ai.upstage.gateway.api.dto.request;

import ai.upstage.gateway.domain.enums.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrRequestDto {
    private String accidentNumber; // 사고번호
    private String confirmationNumber; // 확정순번
    private List<FileInfo> files; // 파일 리스트
}