CREATE TABLE `address`
(
    `id`              int(11)     NOT NULL AUTO_INCREMENT,
    `city`            varchar(50) NOT NULL,
    `street`          varchar(50) NOT NULL,
    `number`          varchar(20) NOT NULL,
    `zipcode`         varchar(20) NOT NULL,
    `country`         varchar(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE `user`
(
    `id`              int(11)     NOT NULL AUTO_INCREMENT,
    `first_name`      varchar(50) NOT NULL,
    `last_name`       varchar(50) NOT NULL,
    `age`             int(20)     NOT NULL,
    `date_of_birth`   date        NOT NULL,
    `address_id`      int(11)     NOT NULL,
    `title`           varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `address_id_idx` (`address_id`),
    CONSTRAINT `address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
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
