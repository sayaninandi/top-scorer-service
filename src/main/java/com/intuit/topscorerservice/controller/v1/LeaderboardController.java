package com.intuit.topscorerservice.controller.v1;


import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import com.intuit.topscorerservice.service.PlayerScoreService;
import com.intuit.topscorerservice.service.impl.CacheHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/scores")
public class LeaderboardController {

    private final PlayerScoreService playerScoreService;
    public static final String GAME_ID = "game1";

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerScoreResponse> save(@RequestBody PlayerScoreRequest request) {
        PlayerScoreResponse playerScoreResponse = playerScoreService.saveScore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerScoreResponse);
    }

    @GetMapping("/top")
    public ResponseEntity<List<PlayerScoreResponse>> getTopKScore(@RequestParam Optional<String> gameId) {
        List<PlayerScoreResponse> topK = playerScoreService.getTopK(gameId.orElse(GAME_ID));
        return ResponseEntity.status(HttpStatus.OK).body(topK);
    }
}
