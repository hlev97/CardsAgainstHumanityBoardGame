package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.*;

public interface GameUserInterface {
    void ShowWhiteCard(User player, White[] cards);
    void Vote(User player, User target);
}
