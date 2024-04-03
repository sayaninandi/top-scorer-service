package com.intuit.topscorerservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@Value
@JsonDeserialize(builder = PlayerScoreResponse.PlayerScoreResponseBuilder.class)
public class PlayerScoreResponse {
    long score;
    String playerId;

    public PlayerScoreResponse( long score,String playerId) {
        this.score = score;
        this.playerId = playerId;
    }
}
