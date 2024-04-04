package com.intuit.topscorerservice.service.impl;

import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import com.intuit.topscorerservice.dao.mapstruct.PlayerScoreEntityMapper;
import com.intuit.topscorerservice.dao.repository.PlayerScoreRepository;
import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import com.intuit.topscorerservice.service.PlayerScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PlayerScoreServiceImpl implements PlayerScoreService {

    private final PlayerScoreEntityMapper mapper = PlayerScoreEntityMapper.mapper;

    private int topK;

    private final PlayerScoreRepository scoreRepository;
    private final CacheHandler cacheHandler;

    public PlayerScoreServiceImpl(@Value("${topK.count:5}") int topK, PlayerScoreRepository scoreRepository, CacheHandler cacheHandler) {
        this.topK = topK;
        this.scoreRepository = scoreRepository;
        this.cacheHandler = cacheHandler;
    }

    @Override
    public List<PlayerScoreResponse> getTopK(String gameId) {
        log.info("Getting top {} scores", topK);
        try {
            List<PlayerScoreResponse> cacheData = cacheHandler.getSortedSetRange(gameId);
            if (cacheData == null || cacheData.size() < 1) {
                return mapper.toDto(scoreRepository.getTopKScore(topK));
            }
            log.info("Returning data from cache");
            return cacheData;
        } catch (Exception e) {
            log.error("Error getting topK", e);
            throw e;
        }
    }


    @Override
    @Transactional
    public void save(PlayerScoreEntity entity) {
        log.info("Saving score");
        scoreRepository.saveScore(entity);
        log.info("Score saved");
    }
    @Override
    public PlayerScoreResponse saveScore(PlayerScoreRequest request) {
        PlayerScoreEntity playerScore = scoreRepository.getPlayerScore(request.getPlayerId(), request.getGameId());

        if(playerScore== null || request.getScore() > playerScore.getScore()) {
            PlayerScoreEntity entity = mapper.toEntity(request);
            save(entity);
            cacheHandler.add(entity);
            return mapper.toDto(entity);
        }

        return mapper.toDto(playerScore);
    }
}
