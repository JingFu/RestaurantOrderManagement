drop table if exists `auth_user`; 
drop table if exists `processing_dish`; 
drop table if exists `processing_order`; 
drop table if exists `completed_dish`; 
drop table if exists `completed_order`;


create table if not exists `auth_user` (
  `user_id` tinyint unsigned not null auto_increment unique, 
  `user_name` varchar(30) not null,
  `password` varchar(30) not null,
  `role` enum('ADMIN', 'CHEF', 'WAITING_STAFF', 'MANAGER') not null,
  primary key (`user_id`)
);


create table if not exists `processing_order` (
	`processing_order_id` int unsigned not null auto_increment unique,
	`table_no` tinyint unsigned not null,
	`status` enum('WAITING', 'PROCESSING', 'READY', 'COMPLETED') not null,
	`create_time` datetime default current_timestamp not null,
	`update_time` datetime on update current_timestamp,
	`create_by` varchar(30) not null,
	`update_by` varchar(30),
	primary key (`processing_order_id`)
); 

create index table_index on `processing_order` (`table_no`) using btree;
create index order_status_index on `processing_order` (`status`) using btree;


create table if not exists `processing_dish` (
	`processing_dish_id` int unsigned not null auto_increment unique,
	`processing_order_id` int unsigned not null,
	`dish_type` enum('SMALL_SIZED', 'MEDIUM_SIZED', 'LARGE_SIZED', 'SPECIAL', 'UNIQUE') not null,
	`dish_name` varchar(50) not null,
	`quantity` int unsigned not null,
	`quantity_waiting` int unsigned not null,
	`quantity_processing` int unsigned not null,
	`quantity_ready` int unsigned not null,
	`quantity_completed` int unsigned not null,
	`unit_price` decimal(5,2) unsigned not null,
	`status` enum('WAITING', 'PROCESSING', 'READY', 'COMPLETED') not null,
	`create_time` datetime default current_timestamp not null,
	`update_time` datetime on update current_timestamp,
	`create_by` varchar(30) not null, 
	`update_by` varchar(30),
	primary key (`processing_dish_id`),
	foreign key (`processing_order_id`) references `processing_order`(`processing_order_id`) on delete cascade
);

create index dish_name_index on `processing_dish` (`dish_name`) using btree;
create index dish_status_index on `processing_dish` (`status`) using btree;


create table if not exists `completed_order` (
	`completed_order_id` int unsigned not null auto_increment unique,
	`table_no` tinyint unsigned not null,
	`bill_charge` decimal(8,2) unsigned, 
	`create_time` datetime default current_timestamp not null,
	`create_by` varchar(30) not null,
	primary key (`completed_order_id`)
); 


create table if not exists `completed_dish` (
	`completed_dish_id` int unsigned not null auto_increment unique,
	`completed_order_id` int unsigned not null,
	`dish_type` enum('SMALL_SIZED', 'MEDIUM_SIZED', 'LARGE_SIZED', 'SPECIAL', 'UNIQUE') not null,
	`dish_name` varchar(50) not null,
	`quantity` int unsigned not null,
	`unit_price` decimal(5,2) unsigned not null,
	`create_time` datetime default current_timestamp not null,
	`create_by` varchar(30) not null,
	primary key (`completed_dish_id`),
	foreign key (`completed_order_id`) references `completed_order`(`completed_order_id`) on delete cascade
);
