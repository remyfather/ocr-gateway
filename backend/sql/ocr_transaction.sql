-- OCR 트랜잭션 테이블
CREATE TABLE IF NOT EXISTS ocr_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(50) UNIQUE NOT NULL,
    file_id VARCHAR(100) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_name VARCHAR(255),
    file_size BIGINT,
    mime_type VARCHAR(100),
    document_type VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    callback_url VARCHAR(500),
    extraction_options TEXT,
    metadata TEXT,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    total_processing_time_ms BIGINT,
    
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_file_id (file_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_document_type (document_type)
); 