package com.intuit.topscorerservice.dao.repository;

import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;

import java.util.List;

public interface PlayerScoreRepository {

    List<PlayerScoreEntity> getTopKScore(int k);

    PlayerScoreEntity getPlayerScore(String playedId, String gameId);

    int saveScore(PlayerScoreEntity scoreEntity);

}
