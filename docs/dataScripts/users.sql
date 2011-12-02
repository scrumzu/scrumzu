START TRANSACTION;
USE `scrumzu`;
INSERT INTO `Users` (`username`,`password`,`enabled`,`idUser`,`salt`) VALUES ('admin','6588a5f8fae35b81942dd9cacfdc4e9e2f9e928a',1,1,638850);
INSERT INTO `Users` (`username`,`password`,`enabled`,`idUser`,`salt`) VALUES ('po','f609a8fe924dbe48be6fc0b13d9418af8e9d62f1',1,2,499723);
INSERT INTO `Users` (`username`,`password`,`enabled`,`idUser`,`salt`) VALUES ('sm','2337d877b38fcad5fe67569fc61f0c5154a61811',1,3,347980);
INSERT INTO `Users` (`username`,`password`,`enabled`,`idUser`,`salt`) VALUES ('user','c84ec5df59917ef6c9f4c35e40520e4a300965d0',1,4,498710);
COMMIT