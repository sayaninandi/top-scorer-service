package com.intuit.topscorerservice.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import com.intuit.topscorerservice.service.PlayerScoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LeaderboardController.class)
public class LeaderboardControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerScoreService playerScoreService;

    private final String playerId = "player1";
    private final String gameId = "game1";
    private final long scoreValue = 20;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PlayerScoreResponse score = PlayerScoreResponse.builder()
            .playerId(playerId)
            .score(scoreValue)
            .build();

    private final PlayerScoreRequest request = PlayerScoreRequest.builder()
            .playerId(playerId)
            .gameId(gameId)
            .score(scoreValue)
            .build();

    @Test
    public void testGetTop() throws Exception {
        when(playerScoreService.getTopK("game1")).thenReturn(Collections.singletonList(score));
        mockMvc.perform(get("/v1/scores/top"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetTop_WithGameId() throws Exception {
        when(playerScoreService.getTopK("game2")).thenReturn(Collections.singletonList(score));
        mockMvc.perform(get("/v1/scores/top?gameId=game2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSave() throws Exception {
        when(playerScoreService.saveScore(request)).thenReturn(score);
        mockMvc.perform(
                post("/v1/scores")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.playerId", is(request.getPlayerId())));
    }

}
