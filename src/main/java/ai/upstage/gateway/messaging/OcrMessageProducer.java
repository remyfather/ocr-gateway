package ai.upstage.gateway.messaging;

import ai.upstage.gateway.config.RabbitMQConfig;
import ai.upstage.gateway.model.DocumentClassificationResult;
import ai.upstage.gateway.model.OcrRequestDto;
import ai.upstage.gateway.model.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrMessageProducer {

    private final RabbitTemplate newRabbitTemplate;

    /**
     * OCR 요청을 받아서 문서 타입별로 큐에 분배합니다.
     */
    public void distributeOcrRequest(OcrRequestDto ocrRequest, List<DocumentClassificationResult> classifications) {
        // 문서 타입별로 그룹화
        Map<DocumentType, List<DocumentClassificationResult>> groupedByType = classifications.stream()
                .collect(Collectors.groupingBy(DocumentClassificationResult::getDocumentType));

        // 각 문서 타입별로 큐에 전송
        for (Map.Entry<DocumentType, List<DocumentClassificationResult>> entry : groupedByType.entrySet()) {
            DocumentType documentType = entry.getKey();
            List<DocumentClassificationResult> docs = entry.getValue();

            sendToQueue(documentType, ocrRequest, docs);
        }
    }

    /**
     * 특정 문서 타입의 큐에 메시지를 전송합니다.
     */
    private void sendToQueue(DocumentType documentType, OcrRequestDto ocrRequest,
            List<DocumentClassificationResult> classifications) {
        String routingKey = getRoutingKey(documentType);
        String queueName = getQueueName(documentType);

        // 큐에 전송할 메시지 생성
        OcrQueueMessage message = new OcrQueueMessage(
                ocrRequest.getAccidentNumber(),
                ocrRequest.getConfirmationNumber(),
                classifications);

        try {
            newRabbitTemplate.convertAndSend(RabbitMQConfig.OCR_EXCHANGE, routingKey, message);
            log.info("메시지 전송 완료 - 큐: {}, 문서 타입: {}, 파일 수: {}",
                    queueName, documentType, classifications.size());
        } catch (Exception e) {
            log.error("메시지 전송 실패 - 큐: {}, 문서 타입: {}", queueName, documentType, e);
            throw new RuntimeException("메시지 전송 실패", e);
        }
    }

    private String getRoutingKey(DocumentType documentType) {
        switch (documentType) {
            case MEDICAL_RECEIPT:
                return RabbitMQConfig.MEDICAL_RECEIPT_ROUTING_KEY;
            case MEDICAL_CERTIFICATE:
                return RabbitMQConfig.MEDICAL_CERTIFICATE_ROUTING_KEY;
            case ADMISSION_DISCHARGE:
                return RabbitMQConfig.ADMISSION_DISCHARGE_ROUTING_KEY;
            case SURGERY_CONFIRMATION:
                return RabbitMQConfig.SURGERY_CONFIRMATION_ROUTING_KEY;
            default:
                throw new IllegalArgumentException("Unknown document type: " + documentType);
        }
    }

    private String getQueueName(DocumentType documentType) {
        switch (documentType) {
            case MEDICAL_RECEIPT:
                return RabbitMQConfig.MEDICAL_RECEIPT_QUEUE;
            case MEDICAL_CERTIFICATE:
                return RabbitMQConfig.MEDICAL_CERTIFICATE_QUEUE;
            case ADMISSION_DISCHARGE:
                return RabbitMQConfig.ADMISSION_DISCHARGE_QUEUE;
            case SURGERY_CONFIRMATION:
                return RabbitMQConfig.SURGERY_CONFIRMATION_QUEUE;
            default:
                throw new IllegalArgumentException("Unknown document type: " + documentType);
        }
    }

    /**
     * 큐에 전송할 메시지 클래스
     */
    public static class OcrQueueMessage {
        private String accidentNumber;
        private String confirmationNumber;
        private List<DocumentClassificationResult> documents;

        public OcrQueueMessage() {
        }

        public OcrQueueMessage(String accidentNumber, String confirmationNumber,
                List<DocumentClassificationResult> documents) {
            this.accidentNumber = accidentNumber;
            this.confirmationNumber = confirmationNumber;
            this.documents = documents;
        }

        // Getters and Setters
        public String getAccidentNumber() {
            return accidentNumber;
        }

        public void setAccidentNumber(String accidentNumber) {
            this.accidentNumber = accidentNumber;
        }

        public String getConfirmationNumber() {
            return confirmationNumber;
        }

        public void setConfirmationNumber(String confirmationNumber) {
            this.confirmationNumber = confirmationNumber;
        }

        public List<DocumentClassificationResult> getDocuments() {
            return documents;
        }

        public void setDocuments(List<DocumentClassificationResult> documents) {
            this.documents = documents;
        }
    }
}