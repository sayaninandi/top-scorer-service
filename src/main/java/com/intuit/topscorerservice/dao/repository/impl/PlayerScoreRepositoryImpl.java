package com.intuit.topscorerservice.dao.repository.impl;

import com.intuit.topscorerservice.dao.Queries;
import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import com.intuit.topscorerservice.dao.mapper.PlayerScoreDaoMapper;
import com.intuit.topscorerservice.dao.repository.PlayerScoreRepository;
import com.intuit.topscorerservice.exception.DaoServiceException;
import com.intuit.topscorerservice.util.exception.codes.DaoExceptionCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PlayerScoreRepositoryImpl implements PlayerScoreRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PlayerScoreDaoMapper mapper;

    @Override
    public List<PlayerScoreEntity> getTopKScore(int k) {
        try {
            return jdbcTemplate.query(Queries.GET_TOP_K, mapper, k);
        } catch (EmptyResultDataAccessException exception) {
            log.info("No data found", exception);
            return List.of();
        } catch (Exception e) {
            log.error("Exception while getting top score", e);
            throw new DaoServiceException(DaoExceptionCodes.DAO_ERROR);
        }
    }

    @Override
    public PlayerScoreEntity getPlayerScore(String playedId, String gameId) {
        try {
            return jdbcTemplate.queryForObject(Queries.GET_PLAYER_SCORE, mapper, playedId, gameId);
        } catch (EmptyResultDataAccessException e) {
            log.info("No data found", e);
            return null;
        } catch (Exception e) {
            log.error("Exception while getting player score", e);
            throw new DaoServiceException(DaoExceptionCodes.DAO_ERROR);
        }
    }

    @Override
    public int saveScore(PlayerScoreEntity scoreEntity) {
        try {

            Integer count = jdbcTemplate.queryForObject(Queries.PLAYER_SCORE_EXIST, Integer.class, scoreEntity.getPlayerId(), scoreEntity.getGameId());
            if (count < 1) {
                int rowsAdded = jdbcTemplate.update(Queries.INSERT_SCORE, scoreEntity.getPlayerId(), scoreEntity.getGameId(), scoreEntity.getScore());
                if (rowsAdded != 1) {
                    log.error("Expected rows added to be 1 when creating score but was {}", rowsAdded);
                    throw new DaoServiceException(DaoExceptionCodes.DAO_UPDATE_ERROR);
                }
                return rowsAdded;
            } else {
                int rowsUpdated = jdbcTemplate.update(Queries.UPDATE_SCORE, scoreEntity.getScore() , scoreEntity.getPlayerId(), scoreEntity.getGameId());
                if (rowsUpdated != 1) {
                    log.error("Expected rows updated to be 1 when updating score but was {}", rowsUpdated);
                    throw new DaoServiceException(DaoExceptionCodes.DAO_UPDATE_ERROR);
                }
                return rowsUpdated;
            }
        } catch (DaoServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception while saving score", e);
            throw new DaoServiceException(DaoExceptionCodes.DAO_UPDATE_ERROR);
        }
    }
}
