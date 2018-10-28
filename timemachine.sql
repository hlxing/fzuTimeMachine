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

 Date: 28/10/2018 09:19:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for time
-- ----------------------------
DROP TABLE IF EXISTS `time`;
CREATE TABLE `time`  (
  `id`          INT(11)                 NOT NULL AUTO_INCREMENT,
  `userId`      INT(11)                 NULL     DEFAULT NULL,
  `imgUrl`      VARCHAR(255)
                CHARACTER SET utf8
                COLLATE utf8_general_ci NULL     DEFAULT NULL,
  `content`     VARCHAR(140)
                CHARACTER SET utf8
                COLLATE utf8_general_ci NULL     DEFAULT NULL,
  `longitude`   DOUBLE(255, 0)          NULL     DEFAULT NULL,
  `latitude`    DOUBLE(255, 0)          NULL     DEFAULT NULL,
  `location`    VARCHAR(255)
                CHARACTER SET utf8
                COLLATE utf8_general_ci NULL     DEFAULT NULL,
  `createTime`  BIGINT(20)              NULL     DEFAULT NULL,
  `updateTime`  BIGINT(20)              NULL     DEFAULT NULL,
  `praiseNum`   INT(11)                 NULL     DEFAULT NULL,
  `checkStatus` TINYINT(1)              NULL     DEFAULT NULL,
  `visible`     TINYINT(1)              NULL     DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3997
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = COMPACT;

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
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for time_praise
-- ----------------------------
DROP TABLE IF EXISTS `time_praise`;
CREATE TABLE `time_praise`  (
  `id`         INT(11)    NOT NULL AUTO_INCREMENT,
  `userId`     INT(11)    NULL     DEFAULT NULL,
  `timeId`     INT(11)    NULL     DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user`  (
  `userId`     int(11)                                                 NOT NULL AUTO_INCREMENT,
  `openId`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickName`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gender`     int(11)                                                 NULL DEFAULT NULL,
  `avatarUrl`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `country`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `province`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `language`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createTime` bigint(20)                                              NULL DEFAULT NULL,
  `sessionId`  VARCHAR(255)
               CHARACTER SET utf8
               COLLATE utf8_general_ci                                 NULL     DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = COMPACT;

SET FOREIGN_KEY_CHECKS = 1;
