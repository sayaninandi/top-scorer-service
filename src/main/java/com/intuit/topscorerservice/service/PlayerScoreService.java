package com.intuit.topscorerservice.service;

import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;

import java.util.List;

public interface PlayerScoreService {

    List<PlayerScoreResponse> getTopK(String gameId);
    PlayerScoreResponse saveScore(PlayerScoreRequest request);
}
