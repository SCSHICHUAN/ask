

CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(20) DEFAULT NULL COMMENT '用户姓名',
  `tell` varchar(20) DEFAULT NULL,
  `yzm` varchar(20) DEFAULT NULL,
  `company` varchar(20) DEFAULT NULL COMMENT '用户的公司',
  `post` varchar(20) DEFAULT NULL COMMENT '用户的职位',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tell` (`tell`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `questions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category` varchar(20) DEFAULT '无',
  `title` varchar(100) NOT NULL,
  `A` varchar(100) DEFAULT NULL,
  `B` varchar(100) DEFAULT NULL,
  `C` varchar(100) DEFAULT NULL,
  `D` varchar(100) DEFAULT NULL,
  `answer` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `answer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` varchar(20) DEFAULT NULL,
  `quID` varchar(20) DEFAULT NULL,
  `category` varchar(20) DEFAULT NULL,
  `answer` varchar(20) DEFAULT NULL,
  `correct` varchar(20) DEFAULT NULL,
  `grade` varchar(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `releases` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tableNam` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `tableNames` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tableNam` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `randomKind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `qNum` int(11) DEFAULT NULL,
  `kindName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE timeAsk(
 time int default 30
)engine=innodb default charset=utf8