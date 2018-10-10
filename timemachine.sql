/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50149
 Source Host           : localhost:3306
 Source Schema         : timemachine

 Target Server Type    : MySQL
 Target Server Version : 50149
 File Encoding         : 65001

 Date: 10/10/2018 21:16:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for time
-- ----------------------------
DROP TABLE IF EXISTS `time`;
CREATE TABLE `time`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NULL DEFAULT NULL,
  `title` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `imgUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `longitude` double(255, 0) NULL DEFAULT NULL,
  `latitude` double(255, 0) NULL DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  `updateTime` bigint(20) NULL DEFAULT NULL,
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `praiseNum` int(11) NULL DEFAULT NULL,
  `checkStatus` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of time
-- ----------------------------
INSERT INTO `time` VALUES (13, 1, 'a nice day', 'cloud.huanglexing.com/17d906da2a0442d482db49a9646bd368', 'a b c d e', 57, 113, 1538579661161, 1538579661161, 'happy', 0, 0);

-- ----------------------------
-- Table structure for time_collection
-- ----------------------------
DROP TABLE IF EXISTS `time_collection`;
CREATE TABLE `time_collection`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NULL DEFAULT NULL,
  `timeId` int(11) NULL DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for time_praise
-- ----------------------------
DROP TABLE IF EXISTS `time_praise`;
CREATE TABLE `time_praise`  (
  `id` int(11) NOT NULL,
  `userId` int(11) NULL DEFAULT NULL,
  `timeId` int(11) NULL DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user`  (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `openId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gender` int(11) NULL DEFAULT NULL,
  `avatarUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `language` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wechat_user
-- ----------------------------
INSERT INTO `wechat_user` VALUES (1, 'oi4T64i99AgPnFrPl4SEz9oaOKFo', 'Hlx', 1, 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKPt3hefZ5CMYbCiag7icR4vUKffpian3Pbtovm54TE5hO2FfY7RTLCDcrSr8EU60hfibnw05LHEXICeA/132', 'China', 'Fujian', 'Xiamen', 'zh_CN', 1538474959);

SET FOREIGN_KEY_CHECKS = 1;
