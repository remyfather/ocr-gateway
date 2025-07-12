package ai.upstage.gateway.messaging;

import ai.upstage.gateway.config.RabbitMQConfig;
import ai.upstage.gateway.model.DocumentClassificationResult;
import ai.upstage.gateway.model.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrMessageConsumer {

    /**
     * 진료비 영수증 큐 컨슈머
     */
    @RabbitListener(queues = RabbitMQConfig.MEDICAL_RECEIPT_QUEUE)
    public void consumeMedicalReceipt(OcrMessageProducer.OcrQueueMessage message) {
        log.info("진료비 영수증 큐에서 메시지 수신 - 사고번호: {}, 확정순번: {}, 파일 수: {}",
                message.getAccidentNumber(), message.getConfirmationNumber(), message.getDocuments().size());

        processOcrDocuments(message, DocumentType.MEDICAL_RECEIPT);
    }

    /**
     * 진단서 큐 컨슈머
     */
    @RabbitListener(queues = RabbitMQConfig.MEDICAL_CERTIFICATE_QUEUE)
    public void consumeMedicalCertificate(OcrMessageProducer.OcrQueueMessage message) {
        log.info("진단서 큐에서 메시지 수신 - 사고번호: {}, 확정순번: {}, 파일 수: {}",
                message.getAccidentNumber(), message.getConfirmationNumber(), message.getDocuments().size());

        processOcrDocuments(message, DocumentType.MEDICAL_CERTIFICATE);
    }

    /**
     * 입퇴원 확인서 큐 컨슈머
     */
    @RabbitListener(queues = RabbitMQConfig.ADMISSION_DISCHARGE_QUEUE)
    public void consumeAdmissionDischarge(OcrMessageProducer.OcrQueueMessage message) {
        log.info("입퇴원 확인서 큐에서 메시지 수신 - 사고번호: {}, 확정순번: {}, 파일 수: {}",
                message.getAccidentNumber(), message.getConfirmationNumber(), message.getDocuments().size());

        processOcrDocuments(message, DocumentType.ADMISSION_DISCHARGE);
    }

    /**
     * 수술확인서 큐 컨슈머
     */
    @RabbitListener(queues = RabbitMQConfig.SURGERY_CONFIRMATION_QUEUE)
    public void consumeSurgeryConfirmation(OcrMessageProducer.OcrQueueMessage message) {
        log.info("수술확인서 큐에서 메시지 수신 - 사고번호: {}, 확정순번: {}, 파일 수: {}",
                message.getAccidentNumber(), message.getConfirmationNumber(), message.getDocuments().size());

        processOcrDocuments(message, DocumentType.SURGERY_CONFIRMATION);
    }

    /**
     * OCR 문서 처리 로직
     */
    private void processOcrDocuments(OcrMessageProducer.OcrQueueMessage message, DocumentType documentType) {
        try {
            List<DocumentClassificationResult> documents = message.getDocuments();

            log.info("{} 문서 처리 시작 - 총 {}개 파일", documentType.getDescription(), documents.size());

            for (DocumentClassificationResult doc : documents) {
                processSingleDocument(doc, message.getAccidentNumber(), message.getConfirmationNumber());
            }

            log.info("{} 문서 처리 완료", documentType.getDescription());

        } catch (Exception e) {
            log.error("{} 문서 처리 중 오류 발생", documentType.getDescription(), e);
            // 여기서 재시도 로직이나 에러 처리 로직을 추가할 수 있습니다.
        }
    }

    /**
     * 단일 문서 처리 로직
     */
    private void processSingleDocument(DocumentClassificationResult doc, String accidentNumber,
            String confirmationNumber) {
        try {
            log.info("문서 처리 중 - 파일명: {}, 파일ID: {}, 문서타입: {}, 신뢰도: {}",
                    doc.getFileInfo().getFileName(),
                    doc.getFileInfo().getFileId(),
                    doc.getDocumentType().getDescription(),
                    doc.getConfidence());

            // TODO: 실제 OCR 처리 로직 구현
            // 1. 파일 다운로드
            // 2. OCR 엔진 호출
            // 3. 결과 파싱
            // 4. 데이터베이스 저장

            // 임시로 처리 시간을 시뮬레이션
            Thread.sleep(1000);

            log.info("문서 처리 완료 - 파일명: {}", doc.getFileInfo().getFileName());

        } catch (Exception e) {
            log.error("문서 처리 실패 - 파일명: {}", doc.getFileInfo().getFileName(), e);
        }
    }
}