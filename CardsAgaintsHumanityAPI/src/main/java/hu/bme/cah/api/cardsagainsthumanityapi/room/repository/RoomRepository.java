package hu.bme.cah.api.cardsagainsthumanityapi.room.repository;

import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.Room;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoomRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Room save(Room room) {
        return em.merge(room);
    }

    @Transactional
    public void save(List<Room> rooms) {
        for (Room room : rooms)
            save(room);
    }

    public List<Room> list() {
        return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }

    public Room findByRoomId(long roomId) {
        String getByIdQuery = "SELECT r FROM Room r WHERE r.roomId=:roomId";
        TypedQuery<Room> query = em.createQuery(getByIdQuery, Room.class).setParameter("roomId", roomId);
        return query.getSingleResult();
    }

    @Transactional
    public void removeByRoomId(long roomId) {
        Room room = findByRoomId(roomId);
        em.remove(room);
    }

    @Transactional
    public Room updateRoomCards(long roomId, Room room) {
        Room result = findByRoomId(roomId);
        if (result == null) throw new EntityNotFoundException();
        else {
            result.setWhiteIds(room.getWhiteIds());
            result.setBlackIds(room.getBlackIds());
            return em.merge(result);
        }
    }

    @Transactional
    public Room updateUsers(long roomId, Room room) {
        Room result = findByRoomId(roomId);
        if (result == null) throw new EntityNotFoundException();
        else {
            result.setAllowedUsers(room.getAllowedUsers());
            return em.merge(result);
        }
    }

    @Transactional
    public Room updateConnectedUsers(long roomId, String name) {
        Room result = findByRoomId(roomId);
        if (result == null) throw new EntityNotFoundException();
        else {
            result.getConnectedUsers().add(name);
            return em.merge(result);
        }
    }
}
