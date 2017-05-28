CREATE TABLE IF NOT EXISTS users (
  id                      BIGINT(20)   NOT NULL AUTO_INCREMENT,
  db_date_created         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP(),
  db_date_updated         TIMESTAMP    NULL ON UPDATE CURRENT_TIMESTAMP(),
  app_created_date        DATETIME     NULL     DEFAULT NULL,
  app_created_by_id       BIGINT(20)            DEFAULT NULL,
  app_last_modified_date  DATETIME     NULL     DEFAULT NULL,
  app_last_modified_by_id BIGINT(20)            DEFAULT NULL,
  email                   VARCHAR(255) NOT NULL,
  first_name              VARCHAR(255)          DEFAULT NULL,
  last_name               VARCHAR(255)          DEFAULT NULL,
  password                VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY users_email_unique_key (email),
  KEY users_created_by_id_foreign_key (app_created_by_id),
  CONSTRAINT users_created_by_id_constraint FOREIGN KEY (app_created_by_id) REFERENCES users (id),
  KEY users_last_modified_by_id_foreign_key (app_last_modified_by_id),
  CONSTRAINT users_last_modified_by_id_constraint FOREIGN KEY (app_last_modified_by_id) REFERENCES users (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

CREATE TABLE projects (
  id                      BIGINT(20)   NOT NULL AUTO_INCREMENT,
  db_date_created         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP(),
  db_date_updated         TIMESTAMP    NULL ON UPDATE CURRENT_TIMESTAMP(),
  app_created_date        DATETIME     NULL     DEFAULT NULL,
  app_created_by_id       BIGINT(20)            DEFAULT NULL,
  app_last_modified_date  DATETIME     NULL     DEFAULT NULL,
  app_last_modified_by_id BIGINT(20)            DEFAULT NULL,
  code                    VARCHAR(255) NOT NULL,
  end_date                DATE                  DEFAULT NULL,
  start_date              DATE         NOT NULL,
  name                    VARCHAR(255)          DEFAULT NULL,
  owner_id                BIGINT(20)   NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY projects_code_unique_key (code),
  KEY projects_created_by_id_foreign_key (app_created_by_id),
  CONSTRAINT projects_created_by_id_constraint FOREIGN KEY (app_created_by_id) REFERENCES users (id),
  KEY projects_last_modified_by_id_foreign_key (app_last_modified_by_id),
  CONSTRAINT projects_last_modified_by_id_constraint FOREIGN KEY (app_last_modified_by_id) REFERENCES users (id),
  KEY projects_owner_id_foreign_key (owner_id),
  CONSTRAINT projects_owner_id_constraint FOREIGN KEY (owner_id) REFERENCES users (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

CREATE TABLE logs (
  id                      BIGINT(20)   NOT NULL AUTO_INCREMENT,
  dtype                   VARCHAR(31)  NOT NULL,
  db_date_created         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP(),
  db_date_updated         TIMESTAMP    NULL ON UPDATE CURRENT_TIMESTAMP(),
  app_created_date        DATETIME     NULL     DEFAULT NULL,
  app_created_by_id       BIGINT(20)            DEFAULT NULL,
  app_last_modified_date  DATETIME     NULL     DEFAULT NULL,
  app_last_modified_by_id BIGINT(20)            DEFAULT NULL,
  category                VARCHAR(255)          DEFAULT NULL,
  date_closed             DATE                  DEFAULT NULL,
  description             LONGTEXT,
  impact                  VARCHAR(255) NOT NULL,
  status                  VARCHAR(255) NOT NULL,
  summary                 LONGTEXT     NOT NULL,
  probability             VARCHAR(255)          DEFAULT NULL,
  risk_response           VARCHAR(255)          DEFAULT NULL,
  owner_id                BIGINT(20)   NOT NULL,
  project_id              BIGINT(20)   NOT NULL,
  PRIMARY KEY (id),
  KEY logs_created_by_id__foreign_key (app_created_by_id),
  CONSTRAINT logs_created_by_id_constraint FOREIGN KEY (app_created_by_id) REFERENCES users (id),
  KEY logs_last_modified_by_id_foreign_key (app_last_modified_by_id),
  CONSTRAINT logs_last_modified_by_id_constraint FOREIGN KEY (app_last_modified_by_id) REFERENCES users (id),
  KEY logs_owner_id_foreign_key (owner_id),
  CONSTRAINT logs_owner_id_constraint FOREIGN KEY (owner_id) REFERENCES users (id),
  KEY logs_project_id_foreign_key (project_id),
  CONSTRAINT logs_project_id_constraint FOREIGN KEY (project_id) REFERENCES projects (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;