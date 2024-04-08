CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       userId VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       nickName VARCHAR(255),
                       isAdmin BOOLEAN,
                       createTime DATETIME,
                       isWithDraw BOOLEAN,
                       status ENUM('USER', 'ADMIN', 'DELETED'),
                       updateTime DATETIME
);