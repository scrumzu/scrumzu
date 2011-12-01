SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `scrumzu` ;
CREATE SCHEMA IF NOT EXISTS `scrumzu` DEFAULT CHARACTER SET utf8 ;
USE `scrumzu` ;

-- -----------------------------------------------------
-- Table `scrumzu`.`Authorities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Authorities` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Authorities` (
  `idAuthority` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `authority` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idAuthority`) ,
  UNIQUE INDEX `idAuthority_UNIQUE` (`idAuthority` ASC) ,
  UNIQUE INDEX `authority_UNIQUE` (`authority` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`Projects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Projects` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Projects` (
  `idProject` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  `owner` VARCHAR(100) NOT NULL ,
  `url` VARCHAR(100) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `version` VARCHAR(20) NOT NULL ,
  `alias` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`idProject`) ,
  UNIQUE INDEX `idProjects_UNIQUE` (`idProject` ASC) ,
  UNIQUE INDEX `alias_UNIQUE` (`alias` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 1895
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`PBIs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`PBIs` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`PBIs` (
  `idPBI` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(100) NOT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `idProject` BIGINT(20) UNSIGNED NOT NULL ,
  `priority` INT(11) NULL DEFAULT NULL ,
  `dateCreation` DATE NOT NULL ,
  `type` TINYINT(4) NOT NULL ,
  PRIMARY KEY (`idPBI`) ,
  UNIQUE INDEX `idPBIs_UNIQUE` (`idPBI` ASC) ,
  INDEX `fk_idProject_PBIs_Projects` (`idProject` ASC) ,
  CONSTRAINT `fk_idProject_PBIs_Projects`
    FOREIGN KEY (`idProject` )
    REFERENCES `scrumzu`.`Projects` (`idProject` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4182
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `scrumzu`.`Sprints`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Sprints` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Sprints` (
  `idSprint` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `dateFrom` DATE NOT NULL ,
  `dateTo` DATE NOT NULL ,
  `sprintStatus` TINYINT(4) NOT NULL ,
  `idProject` BIGINT(20) UNSIGNED NOT NULL ,
  `name` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`idSprint`) ,
  UNIQUE INDEX `idSprints_UNIQUE` (`idSprint` ASC) ,
  INDEX `fk_sprints_projects` (`idProject` ASC) ,
  CONSTRAINT `fk_sprints_projects`
    FOREIGN KEY (`idProject` )
    REFERENCES `scrumzu`.`Projects` (`idProject` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Users` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Users` (
  `idUser` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(30) NOT NULL ,
  `password` VARCHAR(40) NULL DEFAULT NULL ,
  `enabled` TINYINT(1) NULL DEFAULT NULL ,
  `salt` BIGINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`idUser`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`Teams`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Teams` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Teams` (
  `idTeam` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `idProject` BIGINT(20) UNSIGNED NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `name` VARCHAR(50) NOT NULL ,
  `alias` VARCHAR(10) NOT NULL ,
  `idUser` BIGINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`idTeam`) ,
  UNIQUE INDEX `idTeams_UNIQUE` (`idTeam` ASC) ,
  UNIQUE INDEX `key_UNIQUE` (`alias` ASC) ,
  INDEX `fk_idProject_Teams_Projects` (`idProject` ASC) ,
  INDEX `fk_idUsers_Teams_Users` (`idUser` ASC) ,
  CONSTRAINT `fk_idProject_Teams_Projects`
    FOREIGN KEY (`idProject` )
    REFERENCES `scrumzu`.`Projects` (`idProject` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_idUsers_Teams_Users`
    FOREIGN KEY (`idUser` )
    REFERENCES `scrumzu`.`Users` (`idUser` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4176
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`UserPrivileges`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`UserPrivileges` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`UserPrivileges` (
  `idUserPrivilege` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `idAuthority` INT(11) UNSIGNED NOT NULL ,
  `idUser` BIGINT(20) UNSIGNED NOT NULL ,
  PRIMARY KEY (`idUserPrivilege`) ,
  INDEX `fk_UserPrivileges_Authorities1` (`idAuthority` ASC) ,
  INDEX `fk_UserPrivileges_Users1` (`idUser` ASC) ,
  UNIQUE INDEX `idUserPrivilege_UNIQUE` (`idUserPrivilege` ASC) ,
  CONSTRAINT `fk_UserPrivileges_Authorities1`
    FOREIGN KEY (`idAuthority` )
    REFERENCES `scrumzu`.`Authorities` (`idAuthority` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserPrivileges_Users1`
    FOREIGN KEY (`idUser` )
    REFERENCES `scrumzu`.`Users` (`idUser` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`WorkItems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`WorkItems` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`WorkItems` (
  `idWorkItem` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `idTeam` BIGINT(20) UNSIGNED NULL DEFAULT NULL ,
  `idPBI` BIGINT(20) UNSIGNED NOT NULL ,
  `idSprint` BIGINT(20) UNSIGNED NULL ,
  `status` TINYINT(4) NOT NULL ,
  `date` DATETIME NOT NULL ,
  `idUser` BIGINT UNSIGNED NOT NULL ,
  `storyPoints` INT NULL DEFAULT 0 ,
  PRIMARY KEY (`idWorkItem`) ,
  UNIQUE INDEX `idWorkItem_UNIQUE` (`idWorkItem` ASC) ,
  INDEX `fk_idTeam_WorkItems_Teams` (`idTeam` ASC) ,
  INDEX `fk_idPBI_WorkItems_PBIs` (`idPBI` ASC) ,
  INDEX `fk_idSprint_WorkItems_Sprints` (`idSprint` ASC) ,
  INDEX `fk_idUser_WorkItems` (`idUser` ASC) ,
  CONSTRAINT `fk_idPBI_WorkItems_PBIs`
    FOREIGN KEY (`idPBI` )
    REFERENCES `scrumzu`.`PBIs` (`idPBI` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_idSprint_WorkItems_Sprints`
    FOREIGN KEY (`idSprint` )
    REFERENCES `scrumzu`.`Sprints` (`idSprint` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_idTeam_WorkItems_Teams`
    FOREIGN KEY (`idTeam` )
    REFERENCES `scrumzu`.`Teams` (`idTeam` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_idUser_WorkItems`
    FOREIGN KEY (`idUser` )
    REFERENCES `scrumzu`.`Users` (`idUser` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4183
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `scrumzu`.`Attributes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Attributes` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Attributes` (
  `idAttribute` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NOT NULL ,
  `type` TINYINT NOT NULL ,
  `idProject` BIGINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`idAttribute`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  INDEX `fk_Projects_Atributes` (`idProject` ASC) ,
  UNIQUE INDEX `idAtribute_UNIQUE` (`idAttribute` ASC) ,
  CONSTRAINT `fk_Projects_Atributes`
    FOREIGN KEY (`idProject` )
    REFERENCES `scrumzu`.`Projects` (`idProject` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `scrumzu`.`PBIs_StringAttributes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`PBIs_StringAttributes` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`PBIs_StringAttributes` (
  `idAttribute` BIGINT UNSIGNED NOT NULL ,
  `idPBI` BIGINT UNSIGNED NOT NULL ,
  `value` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`idAttribute`, `idPBI`) ,
  INDEX `fk_PBIs_PBIsStringAtributes` (`idPBI` ASC) ,
  INDEX `fk_Atributes_PBIsStringAtributes` (`idAttribute` ASC) ,
  CONSTRAINT `fk_PBIs_PBIsStringAtributes`
    FOREIGN KEY (`idPBI` )
    REFERENCES `scrumzu`.`PBIs` (`idPBI` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Atributes_PBIsStringAtributes`
    FOREIGN KEY (`idAttribute` )
    REFERENCES `scrumzu`.`Attributes` (`idAttribute` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `scrumzu`.`PBIs_DoubleAttributes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`PBIs_DoubleAttributes` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`PBIs_DoubleAttributes` (
  `idAttribute` BIGINT UNSIGNED NOT NULL ,
  `idPBI` BIGINT UNSIGNED NOT NULL ,
  `value` DOUBLE NOT NULL ,
  PRIMARY KEY (`idAttribute`, `idPBI`) ,
  INDEX `fk_PBIs_PBIsDoubleAtributes` (`idPBI` ASC) ,
  INDEX `fk_Atributes_PBIsDoubleAtributes` (`idAttribute` ASC) ,
  CONSTRAINT `fk_PBIs_PBIsDoubleAtributes`
    FOREIGN KEY (`idPBI` )
    REFERENCES `scrumzu`.`PBIs` (`idPBI` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Atributes_PBIsDoubleAtributes`
    FOREIGN KEY (`idAttribute` )
    REFERENCES `scrumzu`.`Attributes` (`idAttribute` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `scrumzu`.`Filters`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Filters` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Filters` (
  `idFilter` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `idUser` BIGINT UNSIGNED NOT NULL ,
  `name` VARCHAR(70) NOT NULL ,
  `isPublic` TINYINT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`idFilter`) ,
  INDEX `fk_username_filters_users` (`idUser` ASC) ,
  UNIQUE INDEX `idFilter_UNIQUE` (`idFilter` ASC) ,
  CONSTRAINT `fk_username_filters_users`
    FOREIGN KEY (`idUser` )
    REFERENCES `scrumzu`.`Users` (`idUser` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Table stores filters for users' ;


-- -----------------------------------------------------
-- Table `scrumzu`.`FilterItems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`FilterItems` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`FilterItems` (
  `idFilterItem` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `idFilter` BIGINT UNSIGNED NOT NULL ,
  `andOr` VARCHAR(5) NULL ,
  `field` VARCHAR(45) NOT NULL ,
  `operator` VARCHAR(5) NOT NULL ,
  `value` VARCHAR(60) NOT NULL ,
  PRIMARY KEY (`idFilterItem`) ,
  INDEX `fk_filter_filterItems_Filters` (`idFilter` ASC) ,
  UNIQUE INDEX `idFilterItem_UNIQUE` (`idFilterItem` ASC) ,
  CONSTRAINT `fk_filter_filterItems_Filters`
    FOREIGN KEY (`idFilter` )
    REFERENCES `scrumzu`.`Filters` (`idFilter` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB, 
COMMENT = 'FilterIems stores details of the user\'s filter' ;


-- -----------------------------------------------------
-- Table `scrumzu`.`Releases`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`Releases` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`Releases` (
  `idRelease` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `dateFrom` DATE NOT NULL ,
  `dateTo` DATE NOT NULL ,
  `name` VARCHAR(100) NOT NULL ,
  `idProject` BIGINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`idRelease`) ,
  UNIQUE INDEX `idReleases_UNIQUE` (`idRelease` ASC) ,
  INDEX `fk_Projects_Releases` (`idProject` ASC) ,
  CONSTRAINT `fk_Projects_Releases`
    FOREIGN KEY (`idProject` )
    REFERENCES `scrumzu`.`Projects` (`idProject` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `scrumzu`.`ReleaseItems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scrumzu`.`ReleaseItems` ;

CREATE  TABLE IF NOT EXISTS `scrumzu`.`ReleaseItems` (
  `idPBI` BIGINT UNSIGNED NOT NULL ,
  `idRelease` BIGINT UNSIGNED NOT NULL ,
  `idUser` BIGINT UNSIGNED NOT NULL ,
  `date` DATE NOT NULL ,
  PRIMARY KEY (`idPBI`, `idRelease`) ,
  INDEX `fk_Users_ReleaseItems` (`idUser` ASC) ,
  INDEX `fk_Releases_ReleaseItems` (`idRelease` ASC) ,
  INDEX `fk_PBIs_ReleaseItems` (`idPBI` ASC) ,
  CONSTRAINT `fk_Users_ReleaseItems`
    FOREIGN KEY (`idUser` )
    REFERENCES `scrumzu`.`Users` (`idUser` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Releases_ReleaseItems`
    FOREIGN KEY (`idRelease` )
    REFERENCES `scrumzu`.`Releases` (`idRelease` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PBIs_ReleaseItems`
    FOREIGN KEY (`idPBI` )
    REFERENCES `scrumzu`.`PBIs` (`idPBI` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
