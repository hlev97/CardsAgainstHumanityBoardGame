package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;

public interface UserRoomInterface {
    void NotifyJoined();
    void NotifyLeft();
    void NotifyKicked();
    void NotifyRoundsSelected(int rounds);
    void NotifySetSelected(Set selectedset);
    void NotifyPlayerJoined(User player);
    void NotifyPlayerLeft(User player);
}
