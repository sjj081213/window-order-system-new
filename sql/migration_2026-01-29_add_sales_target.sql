USE window_order_system;

DROP TABLE IF EXISTS `sales_target`;
CREATE TABLE `sales_target` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `salesperson_id` bigint NOT NULL COMMENT '销售员ID',
  `target_month` varchar(7) NOT NULL COMMENT '目标月份，格式：YYYY-MM',
  `target_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '目标销售额',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sales_month` (`salesperson_id`,`target_month`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售目标表';
