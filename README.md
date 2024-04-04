# top-scorer-service
Intuit Craft Demo project

## Assumptions 
1. The score published by the gaming service is always for a registered user , a registered game and is a legitimate score. There's no requirement for us to verify the score, game or the player provided by the gaming service.
2. Player ranking is determined lexicographically by player_id when two players achieve the same score. playerId values are unique.
3. Player data is updated individually, ensuring no simultaneous updates occur. Each player can only play from one device at a time.
4. Scores are represented as single numerical values. 
5. Upon application startup, a fixed number N is determined to dictate the size of the leaderboard, which returns the top N scores along with player userIdentifier.
6. When a player engages in multiple sessions of the same game, only the highest score attained is stored.
7. A leaderboard is available for each game, with only one game currently available named "game1"
8. MySQL and Redis will always be available.
9. All exposed APIs are exclusively utilized internally by the gaming service. No APIs are publicly accessible. Therefore, we assume these APIs require no data validation checks and are predominantly used in a sequential manner as expected.
10. Since all APIs are utilized internally, user authentication and authorization are unnecessary. All security measures will be managed by the Game service or gateway prior to API access.

## Flow 

1. Admins create a game and register it with the Game service.
    * Upon a player completing a game, the Game service publishes the score to our Leaderboard service.
    * Whenever a game is played (First time), a corresponding leaderboard is available.
2. Upon consumption of scores post-game completion, the leaderboard is dynamically updated.
3. After a game has been played at least once, the top N player details can be fetched at any given time.

## Future Improvement
1. 