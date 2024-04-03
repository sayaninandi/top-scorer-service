package com.intuit.topscorerservice.dao.mapper;

import com.intuit.topscorerservice.dao.Constants;
import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.intuit.topscorerservice.dao.Constants.DATE_CREATED;
import static com.intuit.topscorerservice.dao.Constants.DATE_UPDATED;

@Component
public class PlayerScoreDaoMapper implements RowMapper<PlayerScoreEntity> {

    @Autowired
    private OffsetDateTimeMapper offsetDateTimeMapper;

    @Override
    public PlayerScoreEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PlayerScoreEntity.builder()
                .id(rs.getLong(Constants.ID))
                .score(rs.getLong(Constants.SCORE))
                .playerId(rs.getString(Constants.PLAYED_ID))
                .gameId(rs.getString(Constants.GAME_ID))
                .dateCreated(offsetDateTimeMapper.mapColumn(rs, DATE_CREATED))
                .dateUpdated(offsetDateTimeMapper.mapColumn(rs, DATE_UPDATED))
                .build();
    }
}
