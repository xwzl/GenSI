/*
Navicat MySQL Data Transfer

Source Server         : 192.168.100.122
Source Server Version : 50636
Source Host           : 192.168.100.122:3306
Source Database       : ftoulcloud

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-08-07 10:14:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gsmanage`
-- ----------------------------
DROP TABLE IF EXISTS `gsmanage`;
CREATE TABLE `gsmanage` (
  `sysId` varchar(16) NOT NULL DEFAULT '' COMMENT '系统标识',
  `sysName` varchar(32) DEFAULT NULL COMMENT '系统名称',
  `privateKey` text COMMENT '私有key',
  `publicKey` text COMMENT '公有Key',
  `authedIp` varchar(256) DEFAULT NULL COMMENT '授权的ip，多个ip以英文分号隔开',
  `userName` varchar(32) DEFAULT NULL COMMENT '系统用户名称',
  `pwd` varchar(32) DEFAULT NULL COMMENT '系统用户密码',
  `notifyUrl` varchar(128) DEFAULT NULL COMMENT '消息推送路径',
  `notifyParam` varchar(32) DEFAULT NULL COMMENT '消息推送参数',
  `deleteFlag` varchar(4) DEFAULT NULL COMMENT '删除标识(预留)',
  `reserve1` varchar(64) DEFAULT NULL COMMENT '预留字段1',
  `reserve2` varchar(64) DEFAULT NULL COMMENT '预留字段2',
  PRIMARY KEY (`sysId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of ftmanage
-- ----------------------------

-- ----------------------------
-- Table structure for `gsrequest`
-- ----------------------------
DROP TABLE IF EXISTS `gsrequest`;
CREATE TABLE `gsrequest` (
  `transId` varchar(64) NOT NULL,
  `reqBody` longtext,
  `rspBody` longtext,
  `intime` varchar(32) DEFAULT NULL,
  `serviceCode` varchar(32) NOT NULL,
  `rsptime` varchar(32) DEFAULT NULL,
  `sysId` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`transId`,`serviceCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of fydrequest
-- ----------------------------
