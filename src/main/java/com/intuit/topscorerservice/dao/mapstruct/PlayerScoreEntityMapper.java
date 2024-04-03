package com.intuit.topscorerservice.dao.mapstruct;


import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlayerScoreEntityMapper {
    PlayerScoreEntityMapper mapper = Mappers.getMapper(PlayerScoreEntityMapper.class);

    PlayerScoreResponse toDto(PlayerScoreEntity entity);
    List<PlayerScoreResponse> toDto(List<PlayerScoreEntity> entity);

    PlayerScoreEntity toEntity(PlayerScoreRequest request);
    List<PlayerScoreEntity> toEntity(List<PlayerScoreRequest> requests);

}
