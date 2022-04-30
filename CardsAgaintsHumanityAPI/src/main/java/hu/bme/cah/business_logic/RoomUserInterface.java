package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;

public interface RoomUserInterface {
    void Join(User player);
    void Leave(User player);
    void Kick(User player, User target);
    void SelectSet(Set s);
    void SelectRounds(int rounds);
    void StartGame();
}
