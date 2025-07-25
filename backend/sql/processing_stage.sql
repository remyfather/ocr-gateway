-- 처리 단계 테이블
CREATE TABLE IF NOT EXISTS processing_stages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(50) NOT NULL,
    stage_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    queue_name VARCHAR(100),
    worker_id VARCHAR(50),
    input_data TEXT,
    output_data TEXT,
    error_message TEXT,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    processing_time_ms BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    next_stage_type VARCHAR(50),
    priority INT DEFAULT 0,
    
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_stage_type (stage_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_transaction_stage (transaction_id, stage_type),
    INDEX idx_queue_name (queue_name),
    INDEX idx_worker_id (worker_id)
); 