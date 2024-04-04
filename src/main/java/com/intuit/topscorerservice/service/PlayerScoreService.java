package com.intuit.topscorerservice.service;

import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;

import java.util.List;

public interface PlayerScoreService {

    List<PlayerScoreResponse> getTopK(String gameId);

    void save(PlayerScoreEntity entity);
    PlayerScoreResponse saveScore(PlayerScoreRequest request);
}
