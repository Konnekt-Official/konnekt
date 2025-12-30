/*
    Usage:
        mysql -u root -p < konnekt.sql

        mysql -u root -p
        source konnekt.sql
*/

-- ===============================
-- Database
-- ===============================
CREATE DATABASE IF NOT EXISTS konnekt;
USE konnekt;

-- ===============================
-- User table
-- ===============================
CREATE TABLE IF NOT EXISTS user (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- OTP table
-- ===============================
CREATE TABLE IF NOT EXISTS otp (
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

-- ===============================
-- Enable event scheduler
-- ===============================
SET GLOBAL event_scheduler = ON;

-- ===============================
-- Event: delete expired OTPs
-- ===============================
CREATE EVENT IF NOT EXISTS delete_expired_otps
ON SCHEDULE EVERY 1 MINUTE
DO
    DELETE FROM otp WHERE expires_at <= NOW();

-- ===============================
-- Post table
-- ===============================
CREATE TABLE IF NOT EXISTS post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NOT NULL,
    content TEXT,
    likes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
        REFERENCES user(id)
        ON DELETE CASCADE
);

-- ===============================
-- Comment table
-- ===============================
CREATE TABLE IF NOT EXISTS comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (post_id)
        REFERENCES post(id)
        ON DELETE CASCADE,

    FOREIGN KEY (user_id)
        REFERENCES user(id)
        ON DELETE CASCADE
);

-- ===============================
-- Follow table
-- ===============================
CREATE TABLE IF NOT EXISTS follow (
    id INT AUTO_INCREMENT PRIMARY KEY,
    follower_id INT UNSIGNED NOT NULL,
    following_id INT UNSIGNED NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES user(id) ON DELETE CASCADE
);


-- ===============================
-- Notification table
-- ===============================
CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT PRIMARY KEY,

    user_id INT UNSIGNED NOT NULL,        -- receiver
    sender_id INT UNSIGNED DEFAULT NULL,  -- who triggered it

    type ENUM('LIKE','COMMENT','FOLLOW','MESSAGE') NOT NULL,

    reference_id INT DEFAULT NULL,        -- post_id (LIKE/COMMENT), null for FOLLOW/MESSAGE
    is_read BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES user(id)
        ON DELETE CASCADE,

    FOREIGN KEY (sender_id)
        REFERENCES user(id)
        ON DELETE SET NULL
);