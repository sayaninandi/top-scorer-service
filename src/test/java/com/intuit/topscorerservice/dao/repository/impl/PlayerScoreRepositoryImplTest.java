package com.intuit.topscorerservice.dao.repository.impl;

import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import com.intuit.topscorerservice.dao.mapper.PlayerScoreDaoMapper;
import com.intuit.topscorerservice.exception.DaoServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static com.intuit.topscorerservice.dao.Queries.GET_PLAYER_SCORE;
import static com.intuit.topscorerservice.dao.Queries.GET_TOP_K;
import static com.intuit.topscorerservice.dao.Queries.PLAYER_SCORE_EXIST;
import static com.intuit.topscorerservice.dao.Queries.UPDATE_SCORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlayerScoreRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PlayerScoreRepositoryImpl repository;

    @Captor
    ArgumentCaptor<Object[]> objCap;
    @Captor
    ArgumentCaptor<int[]> intCap;

    private PlayerScoreEntity scoreEntity = PlayerScoreEntity.builder().playerId("playerId").gameId("gameId").score(100).build();

    @Test
    void getTopKScore_ShouldReturnEmptyListWhenNoDataFound() {
        when(jdbcTemplate.query(eq(GET_TOP_K), any(PlayerScoreDaoMapper.class), eq(5)))
                .thenThrow(new EmptyResultDataAccessException(1));

        List<PlayerScoreEntity> result = repository.getTopKScore(5);

        assertTrue(result.isEmpty(), "Expected an empty list when no data is found");
    }

    @Test
    void getPlayerScore_ShouldReturnNullWhenNoDataFound() {
        when(jdbcTemplate.queryForObject(eq(GET_PLAYER_SCORE), any(PlayerScoreDaoMapper.class), eq("playerId"), eq("gameId")))
                .thenThrow(new EmptyResultDataAccessException(1));

        PlayerScoreEntity result = repository.getPlayerScore("playerId", "gameId");

        assertNull(result, "Expected null when no data is found for the player score");
    }

    @Test
    void saveScore_ShouldThrowDaoException() {
        when(jdbcTemplate.queryForObject(eq(PLAYER_SCORE_EXIST), eq(Integer.class), eq("playerId"), eq("gameId")))
                .thenReturn(1);
        when(jdbcTemplate.update(eq(UPDATE_SCORE), eq(100), eq("playerId"), eq("gameId")))
                .thenReturn(1);

        Exception exception = assertThrows(DaoServiceException.class, () -> {
            repository.saveScore(scoreEntity);
        });
        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(DaoServiceException.class);
        assertThat(exception.getMessage()).isEqualTo("DAO_415 : Error while updating DB data");
    }
}