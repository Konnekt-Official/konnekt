/*
    Usage:
        First way:
            mysql -u root -p < <path_to_this_file>

        Second way:
            mysql -u root -p
            source <path_to_this_file>
*/

CREATE DATABASE IF NOT EXISTS konnekt;
USE konnekt;

CREATE TABLE user (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE otp (
    id INT AUTO_INCREMENT PRIMARY KEY,
    otp VARCHAR(10) NOT NULL,
    type ENUM (
        'REGISTER_ACCOUNT',
        'DELETE_ACCOUNT',
        'CHANGE_PASSWORD'
    ) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP AS (created_at + INTERVAL 5 MINUTE) STORED
);

-- Event to delete expired OTPs every 1 minute
SET GLOBAL event_scheduler = ON;

CREATE EVENT IF NOT EXISTS delete_expired_otps
ON SCHEDULE EVERY 1 MINUTE
DO

  DELETE FROM otp WHERE expires_at <= NOW();
