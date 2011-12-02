START TRANSACTION;
USE `scrumzu`;
INSERT INTO `UserPrivileges` (`idUserPrivilege`, `idAuthority`, `idUser`) VALUES (1, 1, 1);
INSERT INTO `UserPrivileges` (`idUserPrivilege`, `idAuthority`, `idUser`) VALUES (2, 2, 2);
INSERT INTO `UserPrivileges` (`idUserPrivilege`, `idAuthority`, `idUser`) VALUES (3, 3, 3);
INSERT INTO `UserPrivileges` (`idUserPrivilege`, `idAuthority`, `idUser`) VALUES (4, 4, 4);
COMMIT