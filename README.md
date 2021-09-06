
## redis 实现点赞设计

### 功能

- 用户点赞的文章
- 用户总点赞的数量
- 某篇文章的点赞数

### 设计

redis 使用Hash结构

- 用户点赞的文章 key（String）：userId ，value（set）：artticleId
- 用户取消点赞的状态：key（String）：userId ，value（set）：artticleId
- 用户总的被点赞数  key：userId  ，value：String  记录总的点赞数
- 某篇文章的点赞 用户 key：articleId ，value：（set）记录用户id

参考

![](https://pic.imgdb.cn/item/6135fedb44eaada739df7395.jpg)



mysql 设计    article文章表 ，中间表。

**将职责划分清楚，文章表只记录文章相关信息，用户表只记录用户，用户点赞文章的信息单独建表**

mysql 数据表 

```mysql
create table `article`(
    `id` bigint(20) not null AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) not null comment '用户id',
    `article_name` varchar(20) not null comment '文章名字',
    `content` BLOB comment '文章内容',
    `total_like` int(11) not null default '0' comment '文章总点赞数',
    `deleted` tinyint(4) not null default '0' comment '是否删除',
    `modified` timestamp not null DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `create` timestamp not null default  current_timestamp comment '生成时间',
                      primary key (id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


create table `user`(
    `id` bigint(20) not null auto_increment primary key comment '主键',
    `user_name` varchar(20) not null  comment '用户名',
    `password` varchar(20) not null comment '密码',
    `deleted` tinyint(4) not null  default '0' comment '是否删除',
    `modified` timestamp not NULL  default  current_timestamp comment '修改时间',
    `create` timestamp not null default current_timestamp comment '生成时间'

)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


create table `user_like_article`(
    `id` bigint(20) not null  auto_increment primary key  comment '主键',
   `user_id` bigint(20) not null  comment '用户id',
    `article_id` bigint(20) not null  comment  '文章id',
    `deleted` tinyint(4) not null  default '0' comment '是否删除',
    `modified` timestamp not NULL  default  current_timestamp comment '修改时间',
    `create` timestamp not null default current_timestamp comment '生成时间'

)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```



