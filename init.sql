SHOW DATABASES;
CREATE DATABASE carpark_info;
USE carpark_info;
DROP TABLE carpark;
CREATE TABLE carpark (
  id varchar(32) NOT NULL COMMENT '停车场编号',
  space int(11) DEFAULT NULL COMMENT '车位总数',
  spot_number varchar(32) DEFAULT NULL COMMENT '车位列表信息',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE ticket (
  id int(11) NOT NULL AUTO_INCREMENT,
  spot_number int(11) NOT NULL COMMENT '车位',
  car_number varchar(32) DEFAULT NULL COMMENT '车牌号',
  carpark_id varchar(32) NOT NULL COMMENT '停车场编号',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE car (
	id int(11) NOT NULL AUTO_INCREMENT,
    car_number varchar(32) DEFAULT NULL COMMENT '车牌号',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;