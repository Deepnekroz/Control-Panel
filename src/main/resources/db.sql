-- MySQL Script generated by MySQL Workbench
-- 10/27/15 19:31:06
-- Model: ControlPanel    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Drop schema controlpanel
-- -----------------------------------------------------
DROP SCHEMA `controlpanel`;

-- -----------------------------------------------------
-- Schema controlpanel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `controlpanel`
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;
USE `controlpanel`;

-- -----------------------------------------------------
-- Table `controlpanel`.`roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`roles`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`roles` (
  `id`         SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '',
  `name`       VARCHAR(255)      NOT NULL
  COMMENT 'User-friendly role name, like \"Admin\"',
  `privileges` BIT(16)           NULL     DEFAULT 0000000000000000
  COMMENT 'Bit-mask access rights. TBD',
  PRIMARY KEY (`id`)
    COMMENT ''
)
  ENGINE = InnoDB
  COMMENT = 'User roles';

CREATE UNIQUE INDEX `name_UNIQUE` ON `controlpanel`.`roles` (`name` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`users`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`users` (
  `id`       BINARY(16) NOT NULL
  COMMENT 'Growing-only random UUID',
  `username` CHAR(64)   NOT NULL
  COMMENT '',
  `password` CHAR(73)   NOT NULL
  COMMENT '<SHA256/64 symb>:<SALT/8 symb>',
  `state`    BIT(1)     NULL DEFAULT 0
  COMMENT 'Inactive(0) or active(1)',
  `email`    CHAR(128)  NULL
  COMMENT '',
  PRIMARY KEY (`id`)
    COMMENT ''
)
  ENGINE = InnoDB
  COMMENT = 'Just a table for all the users';

CREATE UNIQUE INDEX `username_UNIQUE` ON `controlpanel`.`users` (`username` ASC)
  COMMENT '';

CREATE UNIQUE INDEX `email_UNIQUE` ON `controlpanel`.`users` (`email` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`nodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`nodes`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`nodes` (
  `id`        BINARY(16)   NOT NULL
  COMMENT 'Growing-only random UUID',
  `ipaddr`    INT UNSIGNED NOT NULL
  COMMENT 'INET_ATON for INSERT and INET_NTOA for SELECT should be used',
  `osName`    VARCHAR(255) NOT NULL
  COMMENT 'Name of the OS installed',
  `osVersion` VARCHAR(255) NOT NULL
  COMMENT 'Its version',
  `sshId`     VARCHAR(47)  NOT NULL
  COMMENT 'Server SSH public-key fingerprint',
  PRIMARY KEY (`id`)
    COMMENT ''
)
  ENGINE = InnoDB
  COMMENT = 'Our nodes';

CREATE UNIQUE INDEX `ipaddr_UNIQUE` ON `controlpanel`.`nodes` (`ipaddr` ASC)
  COMMENT '';

CREATE UNIQUE INDEX `sshId_UNIQUE` ON `controlpanel`.`nodes` (`sshId` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`role_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`role_user`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`role_user` (
  `idrole` SMALLINT UNSIGNED NOT NULL
  COMMENT '',
  `iduser` BINARY(16)        NOT NULL
  COMMENT '',
  PRIMARY KEY (`idrole`, `iduser`)
    COMMENT '',
  CONSTRAINT `role_user2role`
  FOREIGN KEY (`idrole`)
  REFERENCES `controlpanel`.`roles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `role_user2user`
  FOREIGN KEY (`iduser`)
  REFERENCES `controlpanel`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  COMMENT = 'Role <> User';

CREATE INDEX `role_user2user_idx` ON `controlpanel`.`role_user` (`iduser` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`groups`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`groups`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`groups` (
  `id`   MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '',
  `name` VARCHAR(255)       NOT NULL
  COMMENT '',
  PRIMARY KEY (`id`)
    COMMENT ''
)
  ENGINE = InnoDB
  COMMENT = 'TBD';

CREATE UNIQUE INDEX `name_UNIQUE` ON `controlpanel`.`groups` (`name` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`role_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`role_group`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`role_group` (
  `idrole`  SMALLINT UNSIGNED  NOT NULL
  COMMENT '',
  `idgroup` MEDIUMINT UNSIGNED NOT NULL
  COMMENT '',
  PRIMARY KEY (`idrole`, `idgroup`)
    COMMENT '',
  CONSTRAINT `role_group2role`
  FOREIGN KEY (`idrole`)
  REFERENCES `controlpanel`.`roles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `role_group2group`
  FOREIGN KEY (`idgroup`)
  REFERENCES `controlpanel`.`groups` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  COMMENT = 'Role <> Group';

CREATE INDEX `role_group2group_idx` ON `controlpanel`.`role_group` (`idgroup` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`group_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`group_user`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`group_user` (
  `idgroup` MEDIUMINT UNSIGNED NOT NULL
  COMMENT '',
  `iduser`  BINARY(16)         NOT NULL
  COMMENT '',
  PRIMARY KEY (`idgroup`, `iduser`)
    COMMENT '',
  CONSTRAINT `group_user2group`
  FOREIGN KEY (`idgroup`)
  REFERENCES `controlpanel`.`groups` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `group_user2user`
  FOREIGN KEY (`iduser`)
  REFERENCES `controlpanel`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  COMMENT = 'Group <> User';

CREATE INDEX `group_user2user_idx` ON `controlpanel`.`group_user` (`iduser` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`node_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`node_user`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`node_user` (
  `idnode`       BINARY(16) NOT NULL
  COMMENT '',
  `iduser`       BINARY(16) NOT NULL
  COMMENT '',
  `access_level` BIT(8)     NULL DEFAULT 00000000
  COMMENT 'Bit-mask access rights. TBD',
  PRIMARY KEY (`idnode`, `iduser`)
    COMMENT '',
  CONSTRAINT `node_user2node`
  FOREIGN KEY (`idnode`)
  REFERENCES `controlpanel`.`nodes` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `node_user2user`
  FOREIGN KEY (`iduser`)
  REFERENCES `controlpanel`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  COMMENT = 'Node <> User + Access Level';

CREATE INDEX `node_user2user_idx` ON `controlpanel`.`node_user` (`iduser` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`node_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`node_group`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`node_group` (
  `idnode`       BINARY(16)         NOT NULL
  COMMENT '',
  `idgroup`      MEDIUMINT UNSIGNED NOT NULL
  COMMENT '',
  `access_level` BIT(8)             NULL DEFAULT 00000000
  COMMENT 'Bit-mask access rights. TBD',
  PRIMARY KEY (`idnode`, `idgroup`)
    COMMENT '',
  CONSTRAINT `node_group2node`
  FOREIGN KEY (`idnode`)
  REFERENCES `controlpanel`.`nodes` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `node_group2group`
  FOREIGN KEY (`idgroup`)
  REFERENCES `controlpanel`.`groups` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  COMMENT = 'Node <> Group + Access Level';

CREATE INDEX `node_group2group_idx` ON `controlpanel`.`node_group` (`idgroup` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Table `controlpanel`.`action_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`action_log`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`action_log` (
  `id`     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'Wed probably run out of space before id will overflow',
  `time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP()
  COMMENT 'Time when added',
  `iduser` BINARY(16)      NULL
  COMMENT 'User ID',
  `ipaddr` INT UNSIGNED    NOT NULL
  COMMENT 'Address the request has been made from',
  `action` TEXT            NOT NULL
  COMMENT 'Action that was requested',
  `log`    TEXT            NULL
  COMMENT 'Some info that needs to be stored',
  PRIMARY KEY (`id`)
    COMMENT ''
)
  ENGINE = ARCHIVE
  COMMENT = 'Main log. Read-only';


-- -----------------------------------------------------
-- Table `controlpanel`.`sessions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `controlpanel`.`sessions`;

CREATE TABLE IF NOT EXISTS `controlpanel`.`sessions` (
  `id`     BINARY(16)   NOT NULL
  COMMENT 'Growing-only random UUID',
  `iduser` BINARY(16)   NOT NULL
  COMMENT 'User ID',
  `ipaddr` INT UNSIGNED NOT NULL
  COMMENT 'INET_ATON for INSERT and INET_NTOA for SELECT should be used',
  PRIMARY KEY (`id`, `iduser`)
    COMMENT ''
)
  ENGINE = MEMORY
  COMMENT = 'SID <> UID + IP';

CREATE INDEX `sessions2user_idx` ON `controlpanel`.`sessions` (`iduser` ASC)
  COMMENT '';


-- -----------------------------------------------------
-- Function `ordered_uuid`
-- -----------------------------------------------------
CREATE FUNCTION `ordered_uuid`(uuid BINARY(36))
  RETURNS BINARY(16)
DETERMINISTIC
  RETURN UNHEX(
      CONCAT(
          SUBSTR(uuid, 15, 4),
          SUBSTR(uuid, 10, 4),
          SUBSTR(uuid, 1, 8),
          SUBSTR(uuid, 20, 4),
          SUBSTR(uuid, 25)
      )
  );


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;