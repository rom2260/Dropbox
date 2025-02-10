-- Create database if not exists
CREATE DATABASE IF NOT EXISTS dropbox;

-- Create user with the correct authentication method
DROP USER IF EXISTS 'dropbox_user'@'%';
CREATE USER 'dropbox_user'@'%' IDENTIFIED WITH mysql_native_password BY 'password';
GRANT ALL PRIVILEGES ON dropbox.* TO 'dropbox_user'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    s3_path VARCHAR(512) NOT NULL,
    uploaded_time DATETIME NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);