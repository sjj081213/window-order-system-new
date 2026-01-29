CREATE TABLE `sys_export_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `status` varchar(20) NOT NULL COMMENT '状态: PENDING-待处理, PROCESSING-处理中, COMPLETED-完成, FAILED-失败',
  `file_url` varchar(500) DEFAULT NULL COMMENT '文件下载地址',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `error_msg` text DEFAULT NULL COMMENT '错误信息',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_by` (`create_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导出任务表';
