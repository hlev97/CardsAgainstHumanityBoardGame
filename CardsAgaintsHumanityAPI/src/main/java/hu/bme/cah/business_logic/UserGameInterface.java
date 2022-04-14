package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.*;

import java.util.ArrayList;

public interface UserGameInterface {

    void NotifyGameBegin(ArrayList<User> users, int rounds);
    void NotifyRoundStart(int round, Black blackCard);
    void NotifyVote();
    void NotifyGameEnd(User winner);
    void NotifyKicked();

}
