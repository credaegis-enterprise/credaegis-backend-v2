CREATE TABLE admins
(
    id            VARCHAR(255) NOT NULL,
    admin_id      VARCHAR(255) NOT NULL,
    cluster_id    VARCHAR(255) NOT NULL,
    last_modified datetime NULL,
    CONSTRAINT pk_admins PRIMARY KEY (id)
);

CREATE TABLE approvals
(
    id                        VARCHAR(255) NOT NULL,
    approval_certificate_name VARCHAR(255) NOT NULL,
    recipient_name            VARCHAR(255) NOT NULL,
    recipient_email           VARCHAR(255) NOT NULL,
    expiry_date               date NULL,
    comments                  VARCHAR(255) NULL,
    approval_status           VARCHAR(255) NOT NULL,
    event_id                  VARCHAR(255) NOT NULL,
    created_on                datetime NULL,
    updated_on                datetime NULL,
    CONSTRAINT pk_approvals PRIMARY KEY (id)
);

CREATE TABLE certificates
(
    id               VARCHAR(255) NOT NULL,
    certificate_name VARCHAR(255) NOT NULL,
    certificate_hash VARCHAR(255) NOT NULL,
    recipient_name   VARCHAR(255) NOT NULL,
    recipient_email  VARCHAR(255) NOT NULL,
    issued_date      date         NOT NULL,
    expiry_date      date NULL,
    revoked          BIT(1)       NOT NULL,
    revoked_date     date NULL,
    comments         VARCHAR(255) NULL,
    user_id          VARCHAR(255) NOT NULL,
    event_id         VARCHAR(255) NOT NULL,
    created_on       datetime NULL,
    updated_on       datetime NULL,
    CONSTRAINT pk_certificates PRIMARY KEY (id)
);

CREATE TABLE clusters
(
    id              VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    deactivated     BIT(1)       NOT NULL,
    `locked`        BIT(1)       NOT NULL,
    created_on      datetime NULL,
    updated_on      datetime NULL,
    organization_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_clusters PRIMARY KEY (id)
);

CREATE TABLE events
(
    id            VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    deactivated   BIT(1)       NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    created_by    VARCHAR(255) NOT NULL,
    updated_on    datetime NULL,
    created_on    datetime NULL,
    cluster_id    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id)
);

CREATE TABLE organizations
(
    id      VARCHAR(255) NOT NULL,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    pincode VARCHAR(255) NOT NULL,
    CONSTRAINT pk_organizations PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id      VARCHAR(255) NOT NULL,
    `role`  VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                 VARCHAR(255) NOT NULL,
    username           VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    mfa_enabled        BIT(1)       NOT NULL,
    mfa_secret         VARCHAR(255) NULL,
    brand_logo_enabled BIT(1)       NOT NULL,
    deactivated        BIT(1)       NOT NULL,
    deleted            BIT(1)       NOT NULL,
    organization_id    VARCHAR(255) NOT NULL,
    cluster_id         VARCHAR(255) NULL,
    created_on         datetime NULL,
    updated_on         datetime NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE admins
    ADD CONSTRAINT uc_admins_admin UNIQUE (admin_id);

ALTER TABLE admins
    ADD CONSTRAINT uc_admins_cluster UNIQUE (cluster_id);

ALTER TABLE certificates
    ADD CONSTRAINT uc_certificates_certificate_hash UNIQUE (certificate_hash);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_user UNIQUE (user_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE admins
    ADD CONSTRAINT FK_ADMINS_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES users (id);

ALTER TABLE admins
    ADD CONSTRAINT FK_ADMINS_ON_CLUSTER FOREIGN KEY (cluster_id) REFERENCES clusters (id);

ALTER TABLE approvals
    ADD CONSTRAINT FK_APPROVALS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);

ALTER TABLE certificates
    ADD CONSTRAINT FK_CERTIFICATES_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);

ALTER TABLE certificates
    ADD CONSTRAINT FK_CERTIFICATES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE clusters
    ADD CONSTRAINT FK_CLUSTERS_ON_ORGANIZATION FOREIGN KEY (organization_id) REFERENCES organizations (id);

ALTER TABLE events
    ADD CONSTRAINT FK_EVENTS_ON_CLUSTER FOREIGN KEY (cluster_id) REFERENCES clusters (id);

ALTER TABLE events
    ADD CONSTRAINT FK_EVENTS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE roles
    ADD CONSTRAINT FK_ROLES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CLUSTER FOREIGN KEY (cluster_id) REFERENCES clusters (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ORGANIZATION FOREIGN KEY (organization_id) REFERENCES organizations (id);