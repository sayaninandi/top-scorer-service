package com.intuit.topscorerservice.controller.v1;

import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import com.intuit.topscorerservice.service.PlayerScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LeaderboardControllerTest {

    @Mock
    private PlayerScoreService playerScoreService;

    @InjectMocks
    private LeaderboardController leaderboardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        PlayerScoreRequest request = PlayerScoreRequest.builder().build();
        PlayerScoreResponse response = PlayerScoreResponse.builder().build();
        when(playerScoreService.saveScore(request)).thenReturn(response);

        ResponseEntity<PlayerScoreResponse> result = leaderboardController.save(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(playerScoreService, times(1)).saveScore(request);
    }

    @Test
    public void testGetTopKScore_WithGameId() {
        String gameId = "game123";
        List<PlayerScoreResponse> topK = new ArrayList<>();
        when(playerScoreService.getTopK(gameId)).thenReturn(topK);

        ResponseEntity<List<PlayerScoreResponse>> result = leaderboardController.getTopKScore(Optional.of(gameId));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(topK, result.getBody());
        verify(playerScoreService, times(1)).getTopK(gameId);
    }

}