
CREATE DATABASE IF NOT EXISTS gym;
USE gym;

CREATE TABLE IF NOT EXISTS training_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_type_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255),
    password VARCHAR(255),
    is_active BOOLEAN
);


CREATE TABLE IF NOT EXISTS trainee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    date_of_birth DATE,
    address VARCHAR(255),
    CONSTRAINT fk_trainee_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);


CREATE TABLE IF NOT EXISTS trainer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    training_type_id BIGINT,
    CONSTRAINT fk_trainer_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_trainer_training_type FOREIGN KEY (training_type_id) REFERENCES training_type(id)
);


CREATE TABLE IF NOT EXISTS trainee_trainer (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id BIGINT,
    trainer_id BIGINT,
    CONSTRAINT fk_tt_trainee FOREIGN KEY (trainee_id) REFERENCES trainee(id),
    CONSTRAINT fk_tt_trainer FOREIGN KEY (trainer_id) REFERENCES trainer(id)
);


CREATE TABLE IF NOT EXISTS training (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_name VARCHAR(255),
    training_date VARCHAR(50),
    training_duration INT,
    trainee_id BIGINT,
    trainer_id BIGINT,
    training_type_id BIGINT,
    CONSTRAINT fk_training_trainee FOREIGN KEY (trainee_id) REFERENCES trainee(id),
    CONSTRAINT fk_training_trainer FOREIGN KEY (trainer_id) REFERENCES trainer(id),
    CONSTRAINT fk_training_tt FOREIGN KEY (training_type_id) REFERENCES training_type(id)
);
