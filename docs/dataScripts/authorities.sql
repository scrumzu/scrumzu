START TRANSACTION;
USE `scrumzu`;
INSERT INTO `Authorities` (`idAuthority`,`authority`) VALUES (1,'ROLE_ADMIN');
INSERT INTO `Authorities` (`idAuthority`,`authority`) VALUES (2,'ROLE_PRODUCT_OWNER');
INSERT INTO `Authorities` (`idAuthority`,`authority`) VALUES (3,'ROLE_SCRUM_MASTER');
INSERT INTO `Authorities` (`idAuthority`,`authority`) VALUES (4,'ROLE_TEAM_MEMBER');
COMMIT
