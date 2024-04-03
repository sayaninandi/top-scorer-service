package com.intuit.topscorerservice.dao.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Getter
@Value
@JsonDeserialize(builder = PlayerScoreEntity.PlayerScoreEntityBuilder.class)
public class PlayerScoreEntity {
    long id;
    long score;
    String playerId;
    String gameId;
    OffsetDateTime dateCreated;
    OffsetDateTime dateUpdated;

}
