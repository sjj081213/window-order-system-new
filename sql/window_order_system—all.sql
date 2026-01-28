 
use window_order_system;
 
-- ----------------------------
-- Table structure for after_sales_order
-- ----------------------------
DROP TABLE IF EXISTS `after_sales_order`;
CREATE TABLE `after_sales_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ticket_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工单编号',
  `order_id` bigint NOT NULL COMMENT '关联原订单ID',
  `customer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `customer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上门地址',
  `issue_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '问题描述',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING' COMMENT '状态：PENDING(待处理), ASSIGNED(已指派), PROCESSING(处理中), COMPLETED(已完成), CANCELLED(已取消)',
  `handler_id` bigint NULL DEFAULT NULL COMMENT '处理人ID（通常是安装师傅）',
  `appointment_time` datetime NULL DEFAULT NULL COMMENT '预约上门时间',
  `completion_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `solution` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '解决方案/维修结果',
  `fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '维修费用',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_ticket_no`(`ticket_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_handler_id`(`handler_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '售后工单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of after_sales_order
-- ----------------------------
INSERT INTO `after_sales_order` VALUES (1, 'AS2016368436585828352', 3, '345', '444', '北京市 / 市辖区 / 东城区 444', '123', 'PENDING', 4, '2026-01-31 00:00:00', NULL, NULL, 0.00, 1, '2026-01-28 12:31:12', '2026-01-28 12:31:28', 0);

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除（0：否，1：是）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '窗户品牌表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of brand
-- ----------------------------
INSERT INTO `brand` VALUES (4, '轩尼斯', 1, NULL, 0, '2026-01-26 17:37:43');
INSERT INTO `brand` VALUES (5, '罗普斯金', 1, NULL, 0, '2026-01-26 17:38:00');
INSERT INTO `brand` VALUES (6, '雅韵', 1, NULL, 0, '2026-01-26 17:38:06');
INSERT INTO `brand` VALUES (8, '12_del_8', 1, NULL, 1, '2026-01-27 10:20:58');
INSERT INTO `brand` VALUES (9, '12_del_9', 1, NULL, 1, '2026-01-27 13:36:27');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认地址',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注（如：客户偏好、性格等）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, '孙健', '18764212345', '北京市 / 市辖区 / 东城区 323456', NULL, NULL, '2026-01-26 17:38:36', '2026-01-28 12:36:20', 0);
INSERT INTO `customer` VALUES (2, '123', '222', '北京市 / 市辖区 / 东城区 2222222', NULL, NULL, '2026-01-28 12:21:51', '2026-01-28 12:36:20', 0);
INSERT INTO `customer` VALUES (3, '345', '444', '北京市 / 市辖区 / 东城区 444', NULL, NULL, '2026-01-28 12:24:16', '2026-01-28 12:36:20', 0);

-- ----------------------------
-- Table structure for order_payment
-- ----------------------------
DROP TABLE IF EXISTS `order_payment`;
CREATE TABLE `order_payment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '收款金额',
  `pay_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收款时间',
  `pay_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式（微信、支付宝、现金、转账）',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '操作人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单收款记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_payment
-- ----------------------------
INSERT INTO `order_payment` VALUES (1, 1, 2.00, '2026-01-28 12:34:19', '微信', '', 1, '2026-01-28 12:34:18');

-- ----------------------------
-- Table structure for remeasure_task
-- ----------------------------
DROP TABLE IF EXISTS `remeasure_task`;
CREATE TABLE `remeasure_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `assignee_id` bigint NOT NULL COMMENT '指派师傅ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING（待复尺）、COMPLETED（已完成）',
  `precise_width` double NULL DEFAULT NULL COMMENT '精确宽度',
  `precise_height` double NULL DEFAULT NULL COMMENT '精确高度',
  `sketch_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手绘图URL',
  `site_photos` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '现场图URL列表（JSON）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_assignee_id`(`assignee_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '复尺任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of remeasure_task
-- ----------------------------
INSERT INTO `remeasure_task` VALUES (1, 4, 4, 'PENDING', NULL, NULL, NULL, NULL, '', '2026-01-28 14:18:49', '2026-01-28 14:18:49');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作人ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人用户名',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块名称',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作类型',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名称',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, 6, 'sunjian2', '账号', '保存账号', 'com.window.system.controller.SysUserController.save()', '{\"id\":null,\"username\":\"sunjian211\",\"password\":\"1234567\",\"realName\":\"\",\"role\":\"SALES\",\"currentUserId\":6}', '0:0:0:0:0:0:0:1', '2026-01-27 09:43:32');
INSERT INTO `sys_operation_log` VALUES (2, 6, 'sunjian2', '账号', '删除账号', 'com.window.system.controller.SysUserController.delete()', '8', '0:0:0:0:0:0:0:1', '2026-01-27 09:43:36');
INSERT INTO `sys_operation_log` VALUES (3, 6, 'sunjian2', '品牌', '保存品牌', 'com.window.system.controller.BrandController.save()', '{\"id\":null,\"name\":\"12\",\"currentUserId\":6}', '0:0:0:0:0:0:0:1', '2026-01-27 09:45:38');
INSERT INTO `sys_operation_log` VALUES (4, 6, 'sunjian2', '品牌', '删除品牌', 'com.window.system.controller.BrandController.delete()', '7', '0:0:0:0:0:0:0:1', '2026-01-27 09:45:57');
INSERT INTO `sys_operation_log` VALUES (5, 6, 'sunjian2', '品牌', '保存品牌', 'com.window.system.controller.BrandController.save()', '{\"id\":null,\"name\":\"12\",\"currentUserId\":6}', '0:0:0:0:0:0:0:1', '2026-01-27 10:20:58');
INSERT INTO `sys_operation_log` VALUES (6, 6, 'sunjian2', '品牌', '删除品牌', 'com.window.system.controller.BrandController.delete()', '8', '0:0:0:0:0:0:0:1', '2026-01-27 10:21:42');
INSERT INTO `sys_operation_log` VALUES (7, 6, 'sunjian2', '品牌', '保存品牌', 'com.window.system.controller.BrandController.save()', '{\"id\":null,\"name\":\"12\",\"currentUserId\":6}', '0:0:0:0:0:0:0:1', '2026-01-27 13:36:27');
INSERT INTO `sys_operation_log` VALUES (8, 6, 'sunjian2', '品牌', '删除品牌', 'com.window.system.controller.BrandController.delete()', '9', NULL, '2026-01-27 13:38:18');
INSERT INTO `sys_operation_log` VALUES (9, 6, 'sunjian2', '订单', '创建订单', 'com.window.system.controller.WindowOrderController.create()', '{\"customerName\":\"123\",\"customerPhone\":\"222\",\"address\":\"北京市 / 市辖区 / 东城区 2222222\",\"brand\":\"雅韵\",\"windowType\":\"平开窗\",\"color\":\"白色\",\"glassSpec\":\"12345\",\"width\":4.0,\"height\":5.0,\"price\":300,\"orderTime\":null,\"salespersonId\":6,\"installerId\":4,\"status\":\"SUBMITTED\",\"currentUserId\":6,\"currentUserRole\":\"ADMIN\",\"regionCodes\":\"11,1101,110101\",\"province\":\"11\",\"city\":\"1101\",\"district\":\"110101\",\"detailAddress\":\"2222222\"}', '0:0:0:0:0:0:0:1', '2026-01-28 12:21:51');
INSERT INTO `sys_operation_log` VALUES (10, 3, 'sales_wang', '订单', '创建订单', 'com.window.system.controller.WindowOrderController.create()', '{\"customerName\":\"345\",\"customerPhone\":\"444\",\"address\":\"北京市 / 市辖区 / 东城区 444\",\"brand\":\"雅韵\",\"windowType\":\"内倒窗\",\"color\":\"34\",\"glassSpec\":\"444\",\"width\":1.0,\"height\":1.0,\"price\":100,\"orderTime\":null,\"salespersonId\":3,\"installerId\":4,\"status\":\"SUBMITTED\",\"currentUserId\":3,\"currentUserRole\":\"SALES\",\"regionCodes\":\"11,1101,110101\",\"province\":\"11\",\"city\":\"1101\",\"district\":\"110101\",\"detailAddress\":\"444\"}', '0:0:0:0:0:0:0:1', '2026-01-28 12:24:16');
INSERT INTO `sys_operation_log` VALUES (11, 6, 'sunjian2', '复尺任务', '指派复尺', 'com.window.system.controller.RemeasureTaskController.assign()', '{\"orderId\":4,\"assigneeId\":4,\"remark\":\"\"}', '0:0:0:0:0:0:0:1', '2026-01-28 14:18:49');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码（英文）',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称（中文）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'ADMIN', '管理员', '2026-01-26 10:17:00');
INSERT INTO `sys_role` VALUES (2, 'SALES', '销售员', '2026-01-26 10:17:00');
INSERT INTO `sys_role` VALUES (3, 'INSTALLER', '安装师傅', '2026-01-26 10:21:34');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色：ADMIN（管理员）、SALES（销售员）、INSTALLER（安装师傅）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除（0：否，1：是）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$4xSdvxnIbWscqBkV4Dmz6eMwsk7uTbZum4.4llJ2lnRY8hX8i58ZO', '老板', 'ADMIN', NULL, 1, 0, '2026-01-23 18:24:30');
INSERT INTO `sys_user` VALUES (2, 'sales_chen', '$2a$10$0CGz5TjSDPUdK7ov3yZkk.cZol89Kf8vvYybmlDQ6C7uUCBXRCzxO', '陈女士', 'SALES', 1, 1, 0, '2026-01-23 18:24:30');
INSERT INTO `sys_user` VALUES (3, 'sales_wang', '$2a$10$dCgqRa2Ap.DKJ3ldt8pJ5OJXdZW8UkRjVrGZfz7IWJA5NQ7FtTxK.', '王女士', 'SALES', 1, 1, 0, '2026-01-26 10:06:49');
INSERT INTO `sys_user` VALUES (4, 'install_chen', '$2a$10$hc7//F4gvDtvqQohp2Xo6.XGyvoQ6oXISlu1HhXAb0G2E7fkX.1Za', '陈师傅', 'INSTALLER', 1, 1, 0, '2026-01-26 10:21:34');
INSERT INTO `sys_user` VALUES (5, 'install_sun', '$2a$10$GGIjc05X.6jEULXE.OQ.aeCY4Z9alS95JzMcSrq60ylrcnExnQY2.', '孙师傅', 'INSTALLER', 1, 1, 0, '2026-01-26 10:42:47');
INSERT INTO `sys_user` VALUES (6, 'sunjian2', '$2a$10$MHhdxmcEH07OBh07TX4.zudOzSc9tYjQrMYWNkdmFnpzJDdC1q.YW', '孙健', 'ADMIN', 1, NULL, 0, '2026-01-27 09:40:35');

-- ----------------------------
-- Table structure for window_order
-- ----------------------------
DROP TABLE IF EXISTS `window_order`;
CREATE TABLE `window_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `customer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `customer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户电话',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '关联客户ID',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '安装地址（完整）',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份编码',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市编码',
  `district` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县编码',
  `region_codes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域编码（如：110000,110100）',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户输入的详细地址',
  `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '窗户品牌',
  `window_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '窗型（如：推拉、平开、内倒）',
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '颜色',
  `glass_spec` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '玻璃规格',
  `width` double NULL DEFAULT NULL COMMENT '宽度（毫米）',
  `height` double NULL DEFAULT NULL COMMENT '高度（毫米）',
  `is_remeasured` tinyint(1) NULL DEFAULT 0 COMMENT '是否复尺（0：否，1：是）',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `paid_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '已付金额',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID（未支付）、PARTIAL（部分支付）、PAID（已付清）',
  `order_time` datetime NULL DEFAULT NULL COMMENT '下单时间',
  `install_time` datetime NULL DEFAULT NULL COMMENT '计划安装时间',
  `install_progress` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'WAITING' COMMENT '安装进度：WAITING（待安排）、SCHEDULED（已预约）、INSTALLING（安装中）、FINISHED（已完成）',
  `production_progress` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'WAITING' COMMENT '生产进度：WAITING（待生产）、PRODUCING（生产中）、FINISHED（已完成）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'SUBMITTED' COMMENT '订单状态：DRAFT（草稿）、SUBMITTED（已提交）',
  `salesperson_id` bigint NULL DEFAULT NULL COMMENT '销售员ID',
  `installer_id` bigint NULL DEFAULT NULL COMMENT '安装师傅ID',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除（0：否，1：是）',
  `scheduled_install_date` datetime NULL DEFAULT NULL COMMENT '预约安装日期',
  `actual_install_end_date` datetime NULL DEFAULT NULL COMMENT '实际安装完成日期',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_salesperson`(`salesperson_id` ASC) USING BTREE,
  INDEX `idx_installer`(`installer_id` ASC) USING BTREE,
  INDEX `idx_customer_id`(`customer_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '窗户订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of window_order
-- ----------------------------
INSERT INTO `window_order` VALUES (1, '2015721022740893696', '孙健', '18764212345', 1, '北京市 / 市辖区 / 东城区 323456', '11', '1101', '110101', '11,1101,110101', '323456', '雅韵', NULL, NULL, NULL, 3, 3, 0, 300.00, 2.00, 'PARTIAL', NULL, NULL, 'WAITING', 'PRODUCING', 'SUBMITTED', 1, 4, 1, 1, 0, '2026-02-03 00:00:00', '2026-02-19 00:00:00', '2026-01-26 17:38:36', '2026-01-28 12:36:26');
INSERT INTO `window_order` VALUES (2, '2016366086508908544', '123', '222', 2, '北京市 / 市辖区 / 东城区 2222222', '11', '1101', '110101', '11,1101,110101', '2222222', '雅韵', '平开窗', '白色', '12345', 4, 5, 0, 300.00, 0.00, 'UNPAID', NULL, NULL, 'WAITING', 'WAITING', 'SUBMITTED', 6, 4, 1, 1, 0, NULL, NULL, '2026-01-28 12:21:51', '2026-01-28 12:36:26');
INSERT INTO `window_order` VALUES (3, '2016366690920697856', '345', '444', 3, '北京市 / 市辖区 / 东城区 444', '11', '1101', '110101', '11,1101,110101', '444', '雅韵', '内倒窗', '34', '444', 1, 1, 0, 100.00, 0.00, 'UNPAID', NULL, NULL, 'WAITING', 'WAITING', 'SUBMITTED', 3, 4, 1, 1, 0, NULL, NULL, '2026-01-28 12:24:16', '2026-01-28 12:36:26');
INSERT INTO `window_order` VALUES (4, 'ORD20260128001', '张三', '13800138000', NULL, '北京市朝阳区', NULL, NULL, NULL, NULL, NULL, '凤铝', NULL, NULL, NULL, 1000, 1200, 0, 500.00, 0.00, 'UNPAID', NULL, NULL, 'WAITING', 'WAITING', 'SUBMITTED', 2, NULL, NULL, NULL, 0, NULL, NULL, '2026-01-28 13:11:41', '2026-01-28 13:11:41');

