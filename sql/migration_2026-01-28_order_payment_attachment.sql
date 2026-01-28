-- Migration: add order_payment_attachment table and optional cleanup
USE window_order_system;

CREATE TABLE IF NOT EXISTS `order_payment_attachment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payment_id` bigint NOT NULL COMMENT '支付记录ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '附件URL',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单支付附件表' ROW_FORMAT = DYNAMIC;

-- Optional cleanup: drop old attachments column if it exists (run manually if needed)
-- ALTER TABLE `order_payment` DROP COLUMN `attachments`;
