package hu.bme.cah.api.cardsagainsthumanityapi.room.repository;

import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Repository of CRUD operations
 */
@Slf4j
@Repository
public class RoomRepository {
    @PersistenceContext
    private EntityManager em;

    /**
     * Saves new room to database
     * @param room room
     * @return
     */
    @Transactional
    public Room save(Room room) {
        log.trace("In save(room) method");
        Room savedRoom = em.merge(room);
        log.info("The saving was successful");
        return savedRoom;
    }

    /**
     * Save all given rooms to databse
     * @param rooms rooms
     */
    @Transactional
    public void save(List<Room> rooms) {
        log.trace("In save(rooms) method");
        log.trace("In for cycle");
        for (Room room : rooms)
            save(room);
    }

    /**
     * Reads all room from dadtabase
     * @return rooms
     */
    public List<Room> list() {
        log.trace("In RoomRepository list() method");
        List<Room> rooms = em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
        log.info("Query ran successfully");
        return rooms;
    }

    /**
     * Reads specific room from databse
     * @param roomId id
     * @return room
     */
    public Room findByRoomId(String roomId) {
        log.trace("In RoomRepository findByRoomId(roomId) method");
        Room result = em.find(Room.class, roomId);
        if (result == null) {
            log.error("The room with the given id is not found");
        } else {
            log.info("The room with the given id is found");
        }
        return result;
    }

    /**
     * Deletes a specific room from database
     * @param roomId removable roomId
     */
    @Transactional
    public void removeByRoomId(String roomId) {
        log.trace("In RoomRepository removeByRoomId(roomId) method");
        Room room = findByRoomId(roomId);
        em.remove(room);
    }

    /**
     * Updates room with given id with black and white cards
     * @param roomId updatable roomId
     * @param room room that stores the black and white cards lists
     * @return updated room
     */
    @Transactional
    public Room updateRoomCards(String roomId, Room room) {
        log.trace("In RoomRepository updateRoomCards(roomId, room) method");
        Room result = findByRoomId(roomId);
        if (result == null) {
            log.trace("In if branch");
            log.error("The result is null");
            throw new EntityNotFoundException();
        } else {
            log.trace("In else branch");
            log.info("The result is not null");
            result.setWhiteIds(room.getWhiteIds());
            log.info("White ids have been set successfully");
            result.setBlackIds(room.getBlackIds());
            log.info("Black ids have been set successfully");
            Room updatedRoom = em.merge(result);
            log.info("The room was updated successfully");
            return updatedRoom;
        }
    }

//    @Transactional
//    public Room updateUsers(long roomId, Room room) {
//        Room result = findByRoomId(roomId);
//        if (result == null) throw new EntityNotFoundException();
//        else {
//            result.setAllowedUsers(room.getAllowedUsers());
//            return em.merge(result);
//        }
//    }

    /**
     *  Updates room with given id with a new user
     * @param roomId updatable roomId
     * @param name users name
     * @return updsted room
     */
    @Transactional
    public Room updateConnectedUsers(String roomId, String name) {
        log.trace("In RoomRepository updateConnectedUsers(roomId, name) method");
        Room result = findByRoomId(roomId);
        if (result == null) {
            log.trace("In if branch");
            log.error("The result is null");
            throw new EntityNotFoundException();
        } else {
            log.trace("In else branch");
            log.info("The result is not null");
            result.getConnectedUsers().add(name);
            log.info("ConnectedUsers collection has been updated");
            Room updatedRoom =  em.merge(result);
            log.info("The room was updated successfully");
            return updatedRoom;
        }
    }

    /**
     * Deletes a user from room with givenId
     * @param roomId roomId
     * @param name username
     * @return Room
     */
    @Transactional
    public Room deleteConnectedUsers(String roomId, String name) {
        log.trace("In RoomRepository deleteConnectedUsers(roomId, name) method");
        Room result = findByRoomId(roomId);
        if (result == null) {
            log.trace("In if branch");
            log.error("The result is null");
            throw new EntityNotFoundException();
        } else {
            log.trace("In else branch");
            log.info("The result is not null");
            result.getConnectedUsers().remove(name);
            log.info("The user with the given id has been removed from the room successfully");
            Room updatedRoom = em.merge(result);
            log.info("The room was updated successfully");
            return updatedRoom;
        }
    }

    /**
     * Initializes tha game for a specified room
     * @param roomId roomId
     * @return room
     */
    @Transactional
    public Room initGame(String roomId) {
        log.trace("In RoomRepository initGame(roomId) method");
        Room result = findByRoomId(roomId);
        if (result == null) {
            log.trace("In if branch");
            log.error("The result is null");
            throw new EntityNotFoundException();
        } else {
            log.trace("In else branch");
            log.info("The result is not null");
            Room savedRoom = em.merge(result);
            log.info("The room initialization was successful");
            return savedRoom;
        }
    }
}
