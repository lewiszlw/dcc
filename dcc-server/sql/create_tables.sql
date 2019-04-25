

CREATE TABLE `dcc_admin` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '表自增主键',
  `application` varchar(50) NOT NULL UNIQUE COMMENT '应用',
  `admin_type` varchar(20) NOT NULL DEFAULT '0' COMMENT '管理员类型',
  `admin_id` varchar(20) DEFAULT NULL COMMENT '管理员标识',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';