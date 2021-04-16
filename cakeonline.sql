/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : cakeonline

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2020-10-19 17:35:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cakeinfo`
-- ----------------------------
DROP TABLE IF EXISTS `cakeinfo`;
CREATE TABLE `cakeinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seller_phone` char(11) NOT NULL,
  `cake_name` varchar(100) NOT NULL,
  `cake_price` int(11) NOT NULL,
  `description` varchar(800) DEFAULT NULL,
  `cake_size` int(11) NOT NULL,
  `cake_image` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `seller_phone` (`seller_phone`),
  CONSTRAINT `cakeinfo_ibfk_1` FOREIGN KEY (`seller_phone`) REFERENCES `sellerinfo` (`seller_phone`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cakeinfo
-- ----------------------------
INSERT INTO `cakeinfo` VALUES ('1', '17331280503', '水果蛋糕', '120', '水果巧克力蛋糕是一道由草莓水果，鲜奶油，巧克力海绵蛋糕，鸡蛋，糖等做成的美食。', '8', 'images/g1.png');
INSERT INTO `cakeinfo` VALUES ('2', '17331280503', '寿桃巧克力蛋糕', '79', '寿桃巧克力蛋糕，适于六十大寿。', '8', 'images/g7.png');
INSERT INTO `cakeinfo` VALUES ('3', '17331280503', '黑巧克力蛋糕', '80', '很甜，适合小朋友，浓浓的奶香。', '6', 'images/g10.png');
INSERT INTO `cakeinfo` VALUES ('4', '19831280503', '水果公主蛋糕', '49', '充满童趣，分量足，深受小朋友们喜欢。', '6', 'images/4.jpg');
INSERT INTO `cakeinfo` VALUES ('5', '19831280503', '提拉米苏蛋糕', '49', '小巧但不失风味，深受广大人群喜欢。', '6', 'images/5.jpg');
INSERT INTO `cakeinfo` VALUES ('6', '19831280503', '结婚蛋糕', '189', '浓烈的红玫瑰，象征着浓烈的爱情。', '10', 'images/6.jpg');
INSERT INTO `cakeinfo` VALUES ('7', '19831280503', '黑巧克力蛋糕', '120', '红与黑的碰撞', '8', null);
INSERT INTO `cakeinfo` VALUES ('9', '17331280503', '黑巧克力樱桃蛋糕', '110', '', '8', 'images/g8.png');
INSERT INTO `cakeinfo` VALUES ('12', '17331280503', '草莓蛋糕', '111', '', '8', 'images/g1.png');
INSERT INTO `cakeinfo` VALUES ('13', '17331280503', '红黑蛋糕', '110', '', '8', 'images/img5.jpg');
INSERT INTO `cakeinfo` VALUES ('16', '17331280503', '粉红童趣', '111', '', '6', 'images/11de6e3ea7f24570ba9c6030f25a4cd7.jpg');

-- ----------------------------
-- Table structure for `customerinfo`
-- ----------------------------
DROP TABLE IF EXISTS `customerinfo`;
CREATE TABLE `customerinfo` (
  `customer_phone` char(11) NOT NULL,
  `customer_password` varchar(50) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `customer_photo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`customer_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of customerinfo
-- ----------------------------
INSERT INTO `customerinfo` VALUES ('19831150503', 'ding', '还好吧', '石家庄', null);
INSERT INTO `customerinfo` VALUES ('19831159025', 'ding', '还好', '河北省', 'images\\customerImgs\\1.jpg');
INSERT INTO `customerinfo` VALUES ('19831159055', 'ding', '。', null, null);

-- ----------------------------
-- Table structure for `orderinfo`
-- ----------------------------
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cake_id` int(11) DEFAULT NULL,
  `customer_phone` char(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `flag` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `cake_id` (`cake_id`),
  KEY `customer_phone` (`customer_phone`),
  CONSTRAINT `orderinfo_ibfk_1` FOREIGN KEY (`cake_id`) REFERENCES `cakeinfo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orderinfo_ibfk_2` FOREIGN KEY (`customer_phone`) REFERENCES `customerinfo` (`customer_phone`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of orderinfo
-- ----------------------------
INSERT INTO `orderinfo` VALUES ('2', '1', '19831159025', '3', '2', '1');
INSERT INTO `orderinfo` VALUES ('8', '1', '19831159025', '11', '1', '1');
INSERT INTO `orderinfo` VALUES ('9', '1', '19831159025', '1', '1', '1');
INSERT INTO `orderinfo` VALUES ('10', '1', '19831159025', '1', '2', '1');
INSERT INTO `orderinfo` VALUES ('12', '1', '19831159025', '1', '1', '0');
INSERT INTO `orderinfo` VALUES ('13', '1', '19831159025', '2', '0', '0');
INSERT INTO `orderinfo` VALUES ('14', '1', '19831159025', '3', '0', '0');
INSERT INTO `orderinfo` VALUES ('15', '2', '19831159025', '5', '0', '0');
INSERT INTO `orderinfo` VALUES ('16', '1', '19831159025', '1', '1', '0');
INSERT INTO `orderinfo` VALUES ('17', '1', '19831150503', '4', '0', '0');
INSERT INTO `orderinfo` VALUES ('18', '3', '19831159025', '1', '0', '0');
INSERT INTO `orderinfo` VALUES ('19', '1', '19831159025', '1', '0', '0');
INSERT INTO `orderinfo` VALUES ('20', '1', '19831159025', '9', '1', '0');
INSERT INTO `orderinfo` VALUES ('21', '2', '19831159025', '3', '0', '1');
INSERT INTO `orderinfo` VALUES ('22', '13', '19831159025', '2', '1', '0');
INSERT INTO `orderinfo` VALUES ('23', '1', '19831159025', '4', '0', '0');
INSERT INTO `orderinfo` VALUES ('24', '3', '19831159025', '2', '0', '0');
INSERT INTO `orderinfo` VALUES ('25', '1', '19831159025', '2', '0', '0');
INSERT INTO `orderinfo` VALUES ('26', '3', '19831159025', '2', '0', '0');
INSERT INTO `orderinfo` VALUES ('27', '1', '19831159025', '3', '0', '0');
INSERT INTO `orderinfo` VALUES ('28', '3', '19831159025', '1', '0', '0');
INSERT INTO `orderinfo` VALUES ('29', '1', '19831159055', '2', '0', '1');
INSERT INTO `orderinfo` VALUES ('30', '13', '19831159055', '1', '1', '0');
INSERT INTO `orderinfo` VALUES ('31', '1', '19831159055', '3', '0', '0');
INSERT INTO `orderinfo` VALUES ('32', '9', '19831159055', '2', '1', '0');
INSERT INTO `orderinfo` VALUES ('33', '1', '19831159025', '3', '0', '0');
INSERT INTO `orderinfo` VALUES ('34', '3', '19831159025', '1', '0', '0');
INSERT INTO `orderinfo` VALUES ('35', '1', '19831159025', '1', '0', '0');
INSERT INTO `orderinfo` VALUES ('36', '1', '19831159025', '1', '0', '0');
INSERT INTO `orderinfo` VALUES ('37', '13', '19831159025', '1', '1', '0');
INSERT INTO `orderinfo` VALUES ('38', '3', '19831159025', '1', '1', '1');

-- ----------------------------
-- Table structure for `sellerinfo`
-- ----------------------------
DROP TABLE IF EXISTS `sellerinfo`;
CREATE TABLE `sellerinfo` (
  `seller_phone` char(11) NOT NULL,
  `seller_password` varchar(40) DEFAULT NULL,
  `seller_name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`seller_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sellerinfo
-- ----------------------------
INSERT INTO `sellerinfo` VALUES ('17331280503', 'ding', '丁');
INSERT INTO `sellerinfo` VALUES ('19831280503', 'ding', '丁丁');

-- ----------------------------
-- Table structure for `shoppingcart`
-- ----------------------------
DROP TABLE IF EXISTS `shoppingcart`;
CREATE TABLE `shoppingcart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_phone` char(11) DEFAULT NULL,
  `cake_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_phone` (`customer_phone`),
  KEY `cake_id` (`cake_id`),
  CONSTRAINT `shoppingcart_ibfk_1` FOREIGN KEY (`customer_phone`) REFERENCES `customerinfo` (`customer_phone`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `shoppingcart_ibfk_2` FOREIGN KEY (`cake_id`) REFERENCES `cakeinfo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of shoppingcart
-- ----------------------------
INSERT INTO `shoppingcart` VALUES ('6', '19831159025', '1', '1');
INSERT INTO `shoppingcart` VALUES ('7', '19831159025', '3', '2');
INSERT INTO `shoppingcart` VALUES ('8', '19831159025', '2', '1');
