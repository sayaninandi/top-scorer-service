create table if not exists `player_scores` (
   `id` int NOT NULL AUTO_INCREMENT,
   `player_id` varchar(255) NOT NULL,
    `game_id` varchar(255) NOT NULL,
    `score` int NOT NULL,
    `date_created` timestamp not null default CURRENT_TIMESTAMP,
    `date_updated` timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    primary key (`id`)
    );