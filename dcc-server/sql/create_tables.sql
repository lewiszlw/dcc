CREATE DATABASE IF NOT EXISTS `dcc`;
USE `dcc`;

-- CREATE TABLE `dcc_admin` (
--   `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '表自增主键',
--   `application` varchar(50) NOT NULL COMMENT '应用',
--   `admin_type` varchar(20) NOT NULL DEFAULT 'UNKNOWN' COMMENT '管理员类型',
--   `admin_id` varchar(20) DEFAULT NULL COMMENT '管理员标识',
--   `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
--   PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

CREATE TABLE IF NOT EXISTS `dcc_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '表自增主键',
  `application` varchar(50) NOT NULL DEFAULT '' COMMENT '应用',
  `env` varchar(10) NOT NULL DEFAULT 'UNKNOWN' COMMENT '环境',
  `group` varchar(20) NOT NULL DEFAULT '' COMMENT '分组',
  `key` varchar(50) NOT NULL DEFAULT '' COMMENT '配置key',
  `version` int(10) NOT NULL DEFAULT '0' COMMENT '版本',
  `value` text COMMENT '配置value',
  `comment` varchar(500) DEFAULT NULL COMMENT '注释',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置表';