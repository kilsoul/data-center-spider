
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `type` varchar(32) NOT NULL COMMENT '类型枚举',
  `name` varchar(255) NOT NULL COMMENT '英文简称',
  `name_cn` varchar(255) DEFAULT NULL COMMENT '中文名',
  `path` varchar(255) NOT NULL COMMENT '行政区划路径',
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

-- ----------------------------
-- Table structure for house_base
-- ----------------------------
DROP TABLE IF EXISTS `house_base`;
CREATE TABLE `house_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `house_id` varchar(64) NOT NULL COMMENT '网站id',
  `room_type` varchar(32) DEFAULT NULL COMMENT '房屋户型',
  `floor_info` varchar(2048) DEFAULT NULL COMMENT '楼层信息',
  `construction_area` varchar(32) DEFAULT NULL COMMENT '建筑面积',
  `actual_area` varchar(32) DEFAULT NULL COMMENT '套内实际面积',
  `house_structure` varchar(32) DEFAULT '0' COMMENT '户型结构',
  `building_type` varchar(32) DEFAULT NULL COMMENT '建筑类型',
  `orientation` varchar(32) DEFAULT NULL COMMENT '朝向',
  `building_structure_type` varchar(32) DEFAULT NULL COMMENT '建筑结构类型',
  `decoration_type` varchar(32) DEFAULT NULL COMMENT '装修类型',
  `lift_house_scale` varchar(32) DEFAULT NULL COMMENT '梯户比例',
  `has_lift` tinyint(1) DEFAULT NULL COMMENT '是否有电梯',
  `house_hold_years` varchar(32) DEFAULT NULL COMMENT '产权年限',
  `area_id` bigint(20) NOT NULL COMMENT '区域id',
  `area_info` varchar(64) DEFAULT NULL COMMENT '区域信息',
  `url` varchar(255) DEFAULT NULL COMMENT '页面url',
  `unknown_field` varchar(2048) DEFAULT NULL COMMENT '未知字段json',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_house_id` (`source_type`,`house_id`,`deleted`,`deleted_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for house_brief
-- ----------------------------
DROP TABLE IF EXISTS `house_brief`;
CREATE TABLE `house_brief` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `house_id` varchar(64) NOT NULL COMMENT '网站ID',
  `title` varchar(1024) NOT NULL COMMENT '标题',
  `community_name` varchar(255) DEFAULT NULL COMMENT '小区名称',
  `area_id` bigint(20) NOT NULL COMMENT '位置ID',
  `area_name_cn` varchar(255) DEFAULT NULL COMMENT '位置名称',
  `short_info` varchar(2048) DEFAULT NULL COMMENT '简要信息',
  `follow_info` varchar(255) DEFAULT NULL COMMENT '关注信息',
  `total_price` decimal(10,2) DEFAULT NULL COMMENT '总价，万',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签',
  `detail_url` varchar(2048) DEFAULT NULL COMMENT '明细信息url',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_house_id` (`source_type`,`house_id`,`deleted`,`deleted_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for house_feature
-- ----------------------------
DROP TABLE IF EXISTS `house_feature`;
CREATE TABLE `house_feature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `house_base_id` bigint(20) NOT NULL COMMENT 'house_base.id',
  `description` varchar(2048) DEFAULT NULL COMMENT '小区介绍',
  `room_layout_desc` varchar(2048) DEFAULT NULL COMMENT '户型介绍',
  `decoration_desc` varchar(1024) DEFAULT NULL COMMENT '装修描述',
  `surrounding_facility` varchar(1024) DEFAULT NULL COMMENT '周边配套',
  `suitable_people` varchar(255) DEFAULT NULL COMMENT '适宜人群',
  `traffic_info` varchar(1024) DEFAULT NULL COMMENT '交通情况',
  `taxes_info` varchar(1024) DEFAULT NULL COMMENT '税费信息',
  `sale_info` varchar(1024) DEFAULT NULL COMMENT '销售信息',
  `selling_point` varchar(255) DEFAULT NULL COMMENT '卖点',
  `unknown_field` varchar(2048) DEFAULT NULL COMMENT '未知字段',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_house_base_id` (`source_type`,`house_base_id`,`deleted`,`deleted_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for house_photo
-- ----------------------------
DROP TABLE IF EXISTS `house_photo`;
CREATE TABLE `house_photo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `house_base_id` varchar(64) NOT NULL COMMENT 'house_base.id',
  `name` varchar(255) DEFAULT NULL COMMENT '户型名称',
  `uri` varchar(2048) DEFAULT NULL COMMENT '资源地址',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_house_base_id` (`source_type`,`house_base_id`,`deleted`,`deleted_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for house_room_layout
-- ----------------------------
DROP TABLE IF EXISTS `house_room_layout`;
CREATE TABLE `house_room_layout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `house_base_id` bigint(20) NOT NULL COMMENT 'house_base.id',
  `type` varchar(32) DEFAULT NULL COMMENT '房间类型',
  `area` varchar(32) DEFAULT NULL COMMENT '面积',
  `orientation` varchar(32) DEFAULT NULL COMMENT '朝向',
  `window` varchar(255) DEFAULT NULL COMMENT '窗户类型',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_house_base_id` (`source_type`,`house_base_id`,`deleted`,`deleted_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for house_transaction
-- ----------------------------
DROP TABLE IF EXISTS `house_transaction`;
CREATE TABLE `house_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `house_base_id` bigint(20) NOT NULL COMMENT 'house_base.id',
  `listing_date` date DEFAULT NULL COMMENT '挂牌日期',
  `last_transaction_date` date DEFAULT NULL COMMENT '上次交易日期',
  `transaction_hold_type` varchar(32) DEFAULT NULL COMMENT '交易权属类型，如 商品房',
  `house_usage_type` varchar(32) DEFAULT NULL COMMENT '房屋用途类型，如 普通住宅',
  `house_limit` varchar(32) DEFAULT NULL COMMENT '房屋年限',
  `house_hold_type` varchar(32) DEFAULT NULL COMMENT '产权所属，如 非共有',
  `mortgage_info` varchar(2048) DEFAULT NULL COMMENT '抵押信息',
  `has_mortgage` tinyint(1) DEFAULT NULL COMMENT '是否有抵押',
  `house_credential` varchar(128) DEFAULT NULL COMMENT '房本备件',
  `unknown_field` varchar(2048) DEFAULT NULL COMMENT '未知字段json',
  `created_by` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_by` varchar(64) NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_house_base_id` (`source_type`,`house_base_id`,`deleted`,`deleted_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

