CREATE TABLE player_scores (
    id int(10) unsigned not null auto_increment,
    player_id varchar(255),
    game_id varchar(255),
    score int(10) unsigned not null,
    date_created timestamp(6) not null default CURRENT_TIMESTAMP(6),
    date_updated timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    primary key(id),
    unique key (player_id, game_id),
    key (score)
) default char set = utf8;


