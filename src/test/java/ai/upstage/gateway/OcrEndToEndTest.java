package ai.upstage.gateway;

import ai.upstage.gateway.model.dto.FileProcessRequest;
import ai.upstage.gateway.model.entity.OcrTransaction;
import ai.upstage.gateway.model.enums.DocumentType;
import ai.upstage.gateway.model.enums.OcrTransactionStatus;
import ai.upstage.gateway.repository.OcrTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class OcrEndToEndTest {

        private static final Logger log = LoggerFactory.getLogger(OcrEndToEndTest.class);

        @Autowired
        private WebApplicationContext webApplicationContext;

        @Autowired
        private OcrTransactionRepository ocrTransactionRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private MockMvc mockMvc;

        @Test
        public void testCompleteOcrWorkflow() throws Exception {
                log.info("Starting complete OCR workflow test");
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                // 1. 파일 처리 요청 생성
                FileProcessRequest request = new FileProcessRequest();
                request.setFileId("test-file-001");
                request.setFileUrl("https://example.com/test-file.pdf");
                request.setFileName("test-invoice.pdf");
                request.setFileSize(1024000L);
                request.setMimeType("application/pdf");
                request.setDocumentType(DocumentType.GENERAL);
                request.setCallbackUrl("https://callback.example.com/webhook");

                FileProcessRequest.ExtractionOptions options = new FileProcessRequest.ExtractionOptions();
                options.setExtractText(true);
                options.setExtractTables(true);
                options.setExtractForms(false);
                options.setExtractSignatures(false);
                request.setExtractionOptions(options);

                log.info("Created test request for file: {}", request.getFileId());

                // 2. API 호출하여 처리 시작
                String requestJson = objectMapper.writeValueAsString(request);

                String responseJson = mockMvc.perform(post("/api/ocr/process")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("PENDING"))
                                .andExpect(jsonPath("$.transactionId").exists())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                // 3. 응답에서 트랜잭션 ID 추출
                var response = objectMapper.readTree(responseJson);
                String transactionId = response.get("transactionId").asText();

                log.info("Received transaction ID: {}", transactionId);

                assertNotNull(transactionId);
                assertTrue(transactionId.startsWith("TXN-"));

                // 4. DB에서 트랜잭션 확인
                OcrTransaction transaction = ocrTransactionRepository.findByTransactionId(transactionId);
                assertNotNull(transaction);
                assertEquals(OcrTransactionStatus.PENDING, transaction.getStatus());
                assertEquals(request.getFileId(), transaction.getFileId());
                assertEquals(request.getFileUrl(), transaction.getFileUrl());

                log.info("Transaction confirmed in database with status: {}", transaction.getStatus());

                // 5. 상태 조회 API 테스트
                mockMvc.perform(get("/api/ocr/status/" + transactionId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.transactionId").value(transactionId))
                                .andExpect(jsonPath("$.status").value("PENDING"));

                // 6. 비동기 처리 완료 대기 (실제 환경에서는 더 긴 시간이 필요할 수 있음)
                log.info("Waiting for async processing to complete...");
                TimeUnit.SECONDS.sleep(15);

                // 7. 최종 상태 확인
                mockMvc.perform(get("/api/ocr/status/" + transactionId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.transactionId").value(transactionId))
                                .andExpect(jsonPath("$.status").value("COMPLETED"));

                // 8. DB에서 최종 상태 확인
                transaction = ocrTransactionRepository.findByTransactionId(transactionId);
                assertNotNull(transaction);
                assertEquals(OcrTransactionStatus.COMPLETED, transaction.getStatus());
                assertNotNull(transaction.getCompletedAt());
                assertNotNull(transaction.getTotalProcessingTimeMs());
                assertTrue(transaction.getTotalProcessingTimeMs() > 0);

                log.info("OCR workflow test completed successfully. Processing time: {}ms",
                                transaction.getTotalProcessingTimeMs());
        }

        @Test
        public void testHealthCheck() throws Exception {
                log.info("Testing health check endpoint");
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(get("/api/ocr/health"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("UP"))
                                .andExpect(jsonPath("$.service").value("OCR Gateway"));

                log.info("Health check test passed");
        }

        @Test
        public void testInvalidTransactionId() throws Exception {
                log.info("Testing invalid transaction ID handling");
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                mockMvc.perform(get("/api/ocr/status/invalid-transaction-id"))
                                .andExpect(status().isNotFound());

                log.info("Invalid transaction ID test passed");
        }
}