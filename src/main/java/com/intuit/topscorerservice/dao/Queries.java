package com.intuit.topscorerservice.dao;

public class Queries {

    public static final String GET_TOP_K = """
            select id, player_id, game_id, score, date_created, date_updated 
            from player_scores
            order by score desc limit ?""";

    public static  final String PLAYER_SCORE_EXIST = """
            select count(*)
            from player_scores
            where player_id = ? and game_id=?""";
    public static  final String GET_PLAYER_SCORE = """
            select id, player_id, game_id, score, date_created, date_updated 
            from player_scores
            where player_id = ? and game_id=?""";

    public static  final String SAVE_SCORE = """
            insert into player_scores (player_id, game_id, score)
            values (?, ?, ?)""";

    public static  final String UPDATE_SCORE = """
            update player_scores set score = ?
            where player_id = ? and game_id = ?""";
}
