ALTER TABLE window_order ADD COLUMN logistics_status VARCHAR(20) DEFAULT NULL COMMENT '物流状态：OUTBOUND（已出库）、SHIPPING（送货中）、INBOUND（已入库）' AFTER production_progress;
