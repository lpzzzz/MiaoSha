/*
Navicat MySQL Data Transfer

Source Server         : springboot
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : miaosha

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2018-03-14 14:02:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '商品名称',
  `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(64) DEFAULT NULL COMMENT '商品的图片',
  `goods_detail` longtext COMMENT '商品的详情介绍',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', 'iphoneX', 'Apple iphoneX 手机', '/img/iphonex.png', 'Apple iphoneX 手机 全网通', '8765.00', '10000');
INSERT INTO `goods` VALUES ('2', '华为9', 'Mate 9 4GB', '/img/meta10.png', '华为9 月光银 全网通双卡双待', '3212.00', '10000');

-- ----------------------------
-- Table structure for miaosha_goods
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
  `goods_id` bigint(20) DEFAULT NULL,
  `miaosha_price` decimal(10,2) DEFAULT NULL COMMENT '秒杀单价',
  `stock_count` int(11) DEFAULT NULL COMMENT '库存数量',
  `start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of miaosha_goods
-- ----------------------------
INSERT INTO `miaosha_goods` VALUES ('1', '1', '0.01', '5', '2018-01-29 18:47:00', '2018-02-10 18:50:00');
INSERT INTO `miaosha_goods` VALUES ('2', '2', '0.01', '9', '2017-11-12 00:00:00', '2018-01-31 20:00:00');

-- ----------------------------
-- Table structure for miaosha_order
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_order`;
CREATE TABLE `miaosha_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀订单表',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_uid_gid` (`user_id`,`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of miaosha_order
-- ----------------------------
INSERT INTO `miaosha_order` VALUES ('4', '18348671077', '4', '1');

-- ----------------------------
-- Table structure for miaosha_user
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_user`;
CREATE TABLE `miaosha_user` (
  `id` bigint(20) NOT NULL COMMENT '用户id,手机号码',
  `nickname` varchar(255) NOT NULL COMMENT '登录昵称',
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的id',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of miaosha_user
-- ----------------------------
INSERT INTO `miaosha_user` VALUES ('18348671077', 'dcc', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', null, '2018-01-27 11:05:07', '2018-01-27 11:05:20', '1');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单表',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `delivery_add_id` bigint(20) DEFAULT NULL COMMENT '收获地址id',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` int(11) DEFAULT '0' COMMENT '商品数量',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `order_channel` tinyint(4) DEFAULT NULL COMMENT '1pc,2android,3ios',
  `status` tinyint(4) DEFAULT '0' COMMENT '订单状态：0新建未支付，1待发货，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL COMMENT '订单的创建时间',
  `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('4', '18348671077', '1', null, 'iphoneX', '1', '0.01', '1', '0', '2018-02-09 10:33:19', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'dcc');
