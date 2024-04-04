package com.intuit.topscorerservice.dao.mapper;

import com.intuit.topscorerservice.dao.Constants;
import com.intuit.topscorerservice.dao.entities.PlayerScoreEntity;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlayerScoreDaoMapperTest {

    @Mock
    private OffsetDateTimeMapper offsetDateTimeMapper;

    @InjectMocks
    private PlayerScoreDaoMapper playerScoreDaoMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void mapRow() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong(Constants.ID)).thenReturn(1L);
        when(resultSet.getLong(Constants.SCORE)).thenReturn(100L);
        when(resultSet.getString(Constants.PLAYED_ID)).thenReturn("player1");
        when(resultSet.getString(Constants.GAME_ID)).thenReturn("game1");

        OffsetDateTime dateCreated = OffsetDateTime.now();
        OffsetDateTime dateUpdated = OffsetDateTime.now();

        when(offsetDateTimeMapper.mapColumn(eq(resultSet), anyString())).thenReturn(dateCreated, dateUpdated);

        PlayerScoreEntity result = playerScoreDaoMapper.mapRow(resultSet, 0);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getScore());
        assertEquals("player1", result.getPlayerId());
        assertEquals("game1", result.getGameId());
        assertEquals(dateCreated, result.getDateCreated());
        assertEquals(dateUpdated, result.getDateUpdated());

        verify(resultSet, times(1)).getLong(Constants.ID);
        verify(resultSet, times(1)).getLong(Constants.SCORE);
        verify(resultSet, times(1)).getString(Constants.PLAYED_ID);
        verify(resultSet, times(1)).getString(Constants.GAME_ID);
        verify(offsetDateTimeMapper, times(1)).mapColumn(eq(resultSet), eq(Constants.DATE_CREATED));
        verify(offsetDateTimeMapper, times(1)).mapColumn(eq(resultSet), eq(Constants.DATE_UPDATED));
    }
}