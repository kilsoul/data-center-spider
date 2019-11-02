
CREATE TABLE `area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `type` varchar(32) NOT NULL COMMENT '类型枚举',
  `name` varchar(255) NOT NULL COMMENT '英文简称',
  `name_cn` varchar(255) DEFAULT NULL COMMENT '中文名',
  `url` varchar(1024) NOT NULL COMMENT 'url地址',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父节点',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_type_name` (`source_type`,`type`,`name`,`parent_id`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

