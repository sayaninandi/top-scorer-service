package com.intuit.topscorerservice.service.impl;

import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import com.intuit.topscorerservice.dao.mapstruct.PlayerScoreEntityMapper;
import com.intuit.topscorerservice.dao.repository.PlayerScoreRepository;
import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import com.intuit.topscorerservice.exception.DaoServiceException;
import com.intuit.topscorerservice.util.exception.codes.DaoExceptionCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerScoreServiceImplTest {

    private static final int topK = 5;
    private final PlayerScoreEntityMapper mapper = PlayerScoreEntityMapper.mapper;
    @Mock
    private PlayerScoreRepository scoreRepository;

    @Mock
    private CacheHandler cacheHandler;

    private PlayerScoreServiceImpl playerScoreService;

    private final String gameId = "game123";
    private final String playerId = "player123";
    private PlayerScoreRequest request = PlayerScoreRequest.builder()
            .playerId(playerId)
            .gameId(gameId)
            .score(10)
            .build();

    @BeforeEach
    public void setup() {
        playerScoreService = new PlayerScoreServiceImpl(topK, scoreRepository, cacheHandler);
    }

    @Test
    public void testGetTopK_NoCacheData() {
        PlayerScoreEntity dbData = PlayerScoreEntity.builder().playerId(playerId).score(10).build();
        List<PlayerScoreEntity> scoreEntities = List.of(dbData);

        when(cacheHandler.getSortedSetRange(gameId)).thenReturn(null);
        doReturn(scoreEntities).when(scoreRepository).getTopKScore(topK);

        when(scoreRepository.getTopKScore(topK)).thenReturn(scoreEntities);
        List<PlayerScoreResponse> result = playerScoreService.getTopK(gameId);

        assertEquals(scoreEntities.size(), result.size());
        assertEquals(dbData.getPlayerId(), result.get(0).getPlayerId());
        assertEquals(dbData.getScore(), result.get(0).getScore());
        verify(cacheHandler, times(1)).getSortedSetRange(gameId);
        verify(scoreRepository, times(1)).getTopKScore(topK);
    }

    @Test
    public void testGetTopK_WithCacheData() {
        List<PlayerScoreResponse> cacheData = List.of(PlayerScoreResponse.builder().build());
        when(cacheHandler.getSortedSetRange(gameId)).thenReturn(cacheData);
        List<PlayerScoreResponse> result = playerScoreService.getTopK(gameId);

        assertEquals(cacheData, result);
        verify(cacheHandler, times(1)).getSortedSetRange(gameId);
        verify(scoreRepository, never()).getTopKScore(topK);
    }

    @Test
    public void testGetTopK_WithException() {
        List<PlayerScoreResponse> cacheData = List.of(PlayerScoreResponse.builder().build());
        when(cacheHandler.getSortedSetRange(gameId)).thenReturn(null);
        when(scoreRepository.getTopKScore(topK)).thenThrow(new DaoServiceException(DaoExceptionCodes.DAO_ERROR));

        Exception exception = assertThrows(DaoServiceException.class, () -> {
            playerScoreService.getTopK(gameId);
        });

        assertEquals(DaoExceptionCodes.DAO_ERROR.toString(), exception.getMessage());
    }
    @Test
    void saveScore_newData() {
        PlayerScoreEntity entity = mapper.toEntity(request);
        when(scoreRepository.getPlayerScore(request.getPlayerId(), request.getGameId())).thenReturn(null);
        when(scoreRepository.saveScore(entity)).thenReturn(1);
        doNothing().when(cacheHandler).add(entity);
        PlayerScoreResponse response = playerScoreService.saveScore(request);
        assertEquals(request.getPlayerId(), response.getPlayerId());
        assertEquals(request.getScore(), response.getScore());
    }

    @Test
    void saveScore_notUpdateOnLowScore() {
        long oldScore = 40;
        PlayerScoreEntity oldData = PlayerScoreEntity.builder().playerId(playerId).score(oldScore).build();

        when(scoreRepository.getPlayerScore(request.getPlayerId(), request.getGameId())).thenReturn(oldData);

        PlayerScoreResponse response = playerScoreService.saveScore(request);

        assertEquals(request.getPlayerId(), response.getPlayerId());
        assertEquals(oldScore, response.getScore());
    }
}