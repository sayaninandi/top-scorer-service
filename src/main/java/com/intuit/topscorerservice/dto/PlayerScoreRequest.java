package com.intuit.topscorerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@Value
public class PlayerScoreRequest {
    String playerId;
    String gameId;
    long score;
}
