package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.*;

import java.util.List;

public interface UserGameInterface {

    void NotifyGameBegin(List<User> users, int rounds);
    void NotifyRoundStart(int round, Black blackCard);
    void NotifyVote();
    void NotifyGameEnd(User winner);

}
