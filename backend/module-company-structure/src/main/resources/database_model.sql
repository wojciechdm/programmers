CREATE TABLE `employee`
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

CREATE TABLE `employee_role`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `role`        varchar(20) NOT NULL,
    `level`        varchar(20) NOT NULL,
    `employee_id` int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    KEY `employee_id_idx` (`employee_id`),
    CONSTRAINT `employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE `client`
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
    CONSTRAINT `key_account` FOREIGN KEY (`key_account`) REFERENCES `employee` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE `project`
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
    CONSTRAINT `client_id` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE `project_allocation`
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
    CONSTRAINT `employee_allocated_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE,
    CONSTRAINT `project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

INSERT INTO client(name, code_name, key_account, create_date, last_modification_date)
VALUES ('aaa', 'xxx', null, '2018-09-12', '2019-09-13');

INSERT INTO client(name, code_name, key_account, create_date, last_modification_date)
VALUES ('ttt', 'ppp', null, '2018-09-12', '2019-09-13');

INSERT INTO employee(first_name, last_name, pesel, employment_date, status)
VALUES ('fff', 'eee', null, null, 'CANDIDATE');

INSERT INTO project(name, code_name, start_date, end_date, status, create_date, last_modification_date, client_id)
VALUES ('mmm', 'uuu', '2018-10-14', '2019-02-10', 'ACTIVE', '2019-04-25', '2019-04-26', 2);

