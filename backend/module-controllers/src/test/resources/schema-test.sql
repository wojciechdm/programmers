drop table if exists `user_role`;
drop table if exists `user_login`;
drop table if exists `user`;
drop table if exists `address`;
drop table if exists `project_allocation`;
drop table if exists `project`;
drop table if exists `client`;
drop table if exists `employee_role`;
drop table if exists `employee`;

create TABLE `employee`
(
    `id`              int(11)     NOT NULL AUTO_INCREMENT,
    `first_name`      varchar(50) NOT NULL,
    `last_name`       varchar(50) NOT NULL,
    `pesel`           varchar(11) DEFAULT NULL,
    `employment_date` date        DEFAULT NULL,
    `status`          varchar(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

create TABLE `employee_role`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `role`        varchar(20) NOT NULL,
    `level`        varchar(20) NOT NULL,
    `employee_id` int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    KEY `employee_id_idx` (`employee_id`),
    CONSTRAINT `employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON delete CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

create TABLE `client`
(
    `id`                     int(11)      NOT NULL AUTO_INCREMENT,
    `name`                   varchar(100) NOT NULL,
    `code_name`              varchar(20)  NOT NULL,
    `key_account`            int(11) DEFAULT NULL,
    `create_date`            date         NOT NULL,
    `last_modification_date` date         NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `code_name_UNIQUE` (`code_name`),
    KEY `key_account_idx` (`key_account`),
    CONSTRAINT `key_account` FOREIGN KEY (`key_account`) REFERENCES `employee` (`id`) ON delete SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

create TABLE `project`
(
    `id`                     int(11)      NOT NULL AUTO_INCREMENT,
    `name`                   varchar(100) NOT NULL,
    `code_name`              varchar(20)  NOT NULL,
    `start_date`             date         NOT NULL,
    `end_date`               date DEFAULT NULL,
    `status`                 varchar(20)  NOT NULL,
    `create_date`            date         NOT NULL,
    `last_modification_date` date         NOT NULL,
    `client_id`              int(11)      NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `client_project_unique` (`client_id`, `code_name`),
    KEY `client_id_idx` (`client_id`),
    CONSTRAINT `client_id` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON delete CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

create TABLE `project_allocation`
(
    `id`                       int(11)     NOT NULL AUTO_INCREMENT,
    `project_id`               int(11)     NOT NULL,
    `employee_id`              int(11)     NOT NULL,
    `start_date`               date        NOT NULL,
    `end_date`                 date    DEFAULT NULL,
    `percentile_workload`      int(11) DEFAULT NULL,
    `hours_per_month_workload` int(11) DEFAULT NULL,
    `role`                     varchar(20) NOT NULL,
    `level`                    varchar(20) NOT NULL,
    `rate_monthly`             int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    KEY `project_id_idx` (`project_id`),
    KEY `employee_id_idx` (`employee_id`),
    CONSTRAINT `employee_allocated_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON delete CASCADE,
    CONSTRAINT `project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON delete CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

create TABLE `address`
(
    `id`                        int(11)     NOT NULL AUTO_INCREMENT,
    `city`                      varchar(50) NOT NULL,
    `street`                    varchar(50) NOT NULL,
    `number`                    varchar(20) NOT NULL,
    `zipcode`                   varchar(20) NOT NULL,
    `country`                   varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8
  COLLATE = utf8_general_ci;

create TABLE `user`
(
    `id`                        int(11)     NOT NULL AUTO_INCREMENT,
    `first_name`                varchar(50) NOT NULL,
    `last_name`                 varchar(50) NOT NULL,
    `age`                       int(20)     NOT NULL,
    `date_of_birth`             date        NOT NULL,
    `address_id`                int(11)     NOT NULL,
    `title`                     varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `address_id_idx` (`address_id`),
  CONSTRAINT `address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON delete CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8
  COLLATE = utf8_general_ci;

create TABLE `user_login` (
  `id`                          int(11)     NOT NULL AUTO_INCREMENT,
  `username`                    varchar(40) NOT NULL,
  `password`                    varchar(100)NOT NULL,
  `user_data_id`                int(11)     NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `user_data_id_idx` (`user_data_id`),
  CONSTRAINT `user_data_id` FOREIGN KEY (`user_data_id`) REFERENCES `user` (`id`) ON delete CASCADE ON update CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8
  COLLATE = utf8_general_ci;

create TABLE `user_role` (
    `id`                        int(11)     NOT NULL AUTO_INCREMENT,
    `role`                      varchar(45) NOT NULL,
    `user_id`                   int(11)     NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user_login` (`id`) ON delete CASCADE ON update CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE = utf8_general_ci;

insert into client(name, code_name, key_account, create_date, last_modification_date)
values ('aaa', 'xxx', null, '2018-09-12', '2019-09-13');

insert into client(name, code_name, key_account, create_date, last_modification_date)
values ('ttt', 'ppp', null, '2018-09-12', '2019-09-13');

insert into employee(first_name, last_name, pesel, employment_date, status)
values ('fff', 'eee', null, null, 'CANDIDATE');

insert into employee_role(role, level, employee_id) values ('TESTER', 'JUNIOR', 1);

insert into employee_role(role, level, employee_id) values ('ANALYST', 'JUNIOR', 1);

insert into project(name, code_name, start_date, end_date, status, create_date, last_modification_date, client_id)
values ('mmm', 'uuu', '2018-10-14', '2019-02-10', 'ACTIVE', '2019-04-25', '2019-04-26', 2);

insert into project_allocation(project_id, employee_id, start_date, end_date, percentile_workload,
hours_per_month_workload, role, level, rate_monthly) values (1, 1, '2018-11-10', '2019-01-15', 100, null,
'TESTER', 'JUNIOR', 2000);

insert into address(city, street, number, zipcode, country)
values ('aaa', 'aaa', '33/2', '22-333', 'aaa');

insert into user(first_name, last_name, age, date_of_birth, address_id, title)
values ('aaa', 'bbb', 33, '1986-11-11', 1, 'pan');

insert into user_login(username, password, user_data_id)
values ('zzzzzz', '$2a$10$NaTNE1HMiXgxBxrouj1F0OgfZ0/DsQbYpnxYgC1vrhhaOYM.Lg3ai', 1);

insert into user_role(role, user_id) values ('USER', 1);