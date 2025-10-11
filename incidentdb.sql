CREATE DATABASE incidentdb;
CREATE USER 'incidentuser'@'localhost' IDENTIFIED BY 'strongpassword';
GRANT ALL PRIVILEGES ON incidentdb.* TO 'incidentuser'@'localhost';
FLUSH PRIVILEGES;