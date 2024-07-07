CREATE TABLE `users`
(
    `id`           int                                                           NOT NULL AUTO_INCREMENT,
    `username`     varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '',
    `password`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `email`        varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '',
    `first_name`   varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL,
    `last_name`    varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL,
    `active`       tinyint(1) DEFAULT '1',
    `created_date` datetime                                                               DEFAULT CURRENT_TIMESTAMP,
    `updated_date` datetime                                                               DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `email` (`email`),
    KEY            `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `role`
(
    `id`   int                                                          NOT NULL AUTO_INCREMENT,
    `role` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `role` (`role`),
    KEY    `idx_role` (`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_roles`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL DEFAULT '0',
    `role_id` int NOT NULL default '0',
    PRIMARY KEY (`id`),
    KEY       `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table user_refresh_token
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `user_id`       int(20) not null,
    `refresh_token` text     not null,
    `invoke`        boolean  not null default false,
    `created_date`  datetime not null default CURRENT_TIMESTAMP,
    `update_date`   datetime          default null on update CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY             `idx_user_id_invoke` (`user_id` , invoke) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
