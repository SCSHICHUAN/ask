

/*
  用户表
*/

create table user(
id int unsigned auto_increment key comment'自增ID',
name varchar(20) comment'用户姓名',
tell varchar(20) unique comment'用户电话号码',
yzm  varchar(20) comment'用户密码',
company varchar(20) comment'用户的公司',
post varchar(20) comment'用户的职务'
);

/*
  题目表
*/
create table questions(
id int unsigned auto_increment key,
category varchar(20) default'无',
title varchar(100) not null unique,
A varchar(100),
B varchar(100),
C varchar(100),
D varchar(100),
answer varchar(4)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

/*
  试卷表
*/
create table if not exists ？(
id int unsigned auto_increment key,
questionID varchar(20)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

/*
  试卷名称表
*/
create table if not exists tableNames(
id int unsigned auto_increment key,
tableNam varchar(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
 


/*
  发布的试卷名称表
*/
create table if not exists releases(
id int unsigned auto_increment key,
tableNam varchar(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

/*
  答题表
*/
create table if not exists answer(
id int unsigned AUTO_INCREMENT key,
userID varchar(20),
quID varchar(20),
category varchar(20),
answer varchar(20),
correct varchar(20),
grade varchar(1) default'0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

















 