package hu.bme.cah.api.cardsagainsthumanityapi.room.domain;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;

import java.util.List;
import java.util.Map;

/**
 * Represents game state of a room
 */
public class GameState {
    public String turnState;
    public int currentRound;
    public int allRound;
    public Black black;
    public List<White> whites;
    public Map<String, Integer> scores;
    public Map<String, List<White>> chosenCards;
}
