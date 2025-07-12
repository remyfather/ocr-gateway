package ai.upstage.gateway.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange 이름
    public static final String OCR_EXCHANGE = "ocr.exchange";

    // 새로운 문서 타입별 Queue 이름들
    public static final String MEDICAL_RECEIPT_QUEUE = "medical.receipt.queue";
    public static final String MEDICAL_CERTIFICATE_QUEUE = "medical.certificate.queue";
    public static final String ADMISSION_DISCHARGE_QUEUE = "admission.discharge.queue";
    public static final String SURGERY_CONFIRMATION_QUEUE = "surgery.confirmation.queue";

    // 새로운 문서 타입별 Routing Key들
    public static final String MEDICAL_RECEIPT_ROUTING_KEY = "medical.receipt";
    public static final String MEDICAL_CERTIFICATE_ROUTING_KEY = "medical.certificate";
    public static final String ADMISSION_DISCHARGE_ROUTING_KEY = "admission.discharge";
    public static final String SURGERY_CONFIRMATION_ROUTING_KEY = "surgery.confirmation";

    // Exchange 생성
    @Bean
    public DirectExchange ocrExchange() {
        return new DirectExchange(OCR_EXCHANGE);
    }

    // 진료비 영수증 큐
    @Bean
    public Queue medicalReceiptQueue() {
        return new Queue(MEDICAL_RECEIPT_QUEUE, true);
    }

    // 진단서 큐
    @Bean
    public Queue medicalCertificateQueue() {
        return new Queue(MEDICAL_CERTIFICATE_QUEUE, true);
    }

    // 입퇴원 확인서 큐
    @Bean
    public Queue admissionDischargeQueue() {
        return new Queue(ADMISSION_DISCHARGE_QUEUE, true);
    }

    // 수술확인서 큐
    @Bean
    public Queue surgeryConfirmationQueue() {
        return new Queue(SURGERY_CONFIRMATION_QUEUE, true);
    }

    // Binding 설정
    @Bean
    public Binding medicalReceiptBinding(Queue medicalReceiptQueue, DirectExchange ocrExchange) {
        return BindingBuilder.bind(medicalReceiptQueue).to(ocrExchange).with(MEDICAL_RECEIPT_ROUTING_KEY);
    }

    @Bean
    public Binding medicalCertificateBinding(Queue medicalCertificateQueue, DirectExchange ocrExchange) {
        return BindingBuilder.bind(medicalCertificateQueue).to(ocrExchange).with(MEDICAL_CERTIFICATE_ROUTING_KEY);
    }

    @Bean
    public Binding admissionDischargeBinding(Queue admissionDischargeQueue, DirectExchange ocrExchange) {
        return BindingBuilder.bind(admissionDischargeQueue).to(ocrExchange).with(ADMISSION_DISCHARGE_ROUTING_KEY);
    }

    @Bean
    public Binding surgeryConfirmationBinding(Queue surgeryConfirmationQueue, DirectExchange ocrExchange) {
        return BindingBuilder.bind(surgeryConfirmationQueue).to(ocrExchange).with(SURGERY_CONFIRMATION_ROUTING_KEY);
    }

    // JSON 메시지 컨버터 (새로운 시스템용)
    @Bean("newJsonMessageConverter")
    public MessageConverter newJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate 설정 (새로운 시스템용)
    @Bean("newRabbitTemplate")
    public RabbitTemplate newRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(newJsonMessageConverter());
        return rabbitTemplate;
    }
}