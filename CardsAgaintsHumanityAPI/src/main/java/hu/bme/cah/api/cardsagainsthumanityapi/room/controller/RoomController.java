package hu.bme.cah.api.cardsagainsthumanityapi.room.controller;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagainsthumanityapi.email.service.EmailService;
import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.GameState;
import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.Room;
import hu.bme.cah.api.cardsagainsthumanityapi.room.service.RoomService;
import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.bme.cah.api.cardsagainsthumanityapi.room.domain.Room.*;

/**
 * RestController to give access through {@link RoomService} to {@link Room} cards
 */
@Slf4j
@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @Autowired
    EmailService emailService;

    /**
     * Create new room
     * @param room room
     * @return room
     */
    @PostMapping
    @Secured(User.ROLE_USER)
    public ResponseEntity<Room> create(@RequestBody Room room) {
        log.trace("In RoomController create(room) method");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Checking if name is given and unique");
        if (!(room.getRoomId() != null && room.getRoomId() != "" && roomService.getByRoomId(room.getRoomId()) == null))
        {
            log.info("Name is not given or not unique");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
        }
        log.info("Name is given and unique");
        log.info("Update invoker user with ROLE_CZAR authority");
        roomService.updateUserRoleById(auth.getName(), User.ROLE_CZAR);
        Room result = roomService.createRoom(room, auth.getName());
        log.info("Creation was successful");
        return ResponseEntity.ok(result);
    }

    /**
     * Sending emails
     * @param recipients recipients
     */
    private void sendEmails(List<User> recipients) {
        log.trace("In RoomController sendEmails(recipients) method");
        log.info("Send email to recipients");
        for (User to : recipients) {
            emailService.sendEmail(
                    to.getEmail(),
                    "test",
                    "test"
            );
        }
    }

    /**
     * Get room by given id
     * @param roomId roomId
     * @return room
     */
    @GetMapping("/{roomId}")
    @Secured(User.ROLE_USER)
    public Room getByRoomId(@PathVariable String roomId) {
        log.trace("In RoomController getByRoomId(roomId) method");
        return roomService.getByRoomId(roomId);
    }

    /**
     * Get all room
     * @return rooms
     */
    @GetMapping("/list")
    @Secured(User.ROLE_USER)
    public List<Room> list() {
        log.trace("In RoomController list() method");
        return roomService.list();
    }

    /**
     * Delete room with given id
     * @param roomId id of removable room
     * @return response
     */
    @DeleteMapping("/{roomId}")
    @Secured({User.ROLE_CZAR, User.ROLE_ADMIN})
    public ResponseEntity<?> delete(@PathVariable String roomId) {
        log.trace("In RoomController delete(roomId) method");
        Room room = roomService.getByRoomId(roomId);
        if (room == null) {
            log.trace("In if branch: room is null");
            log.error("Room with the given id was not found");
            return ResponseEntity.notFound().build();
        } else {
            log.trace("In else branch: room is not null");
            roomService.removeByRoomId(roomId);
            log.info("Room with the given id is removed");
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Join to a specific room
     * @param roomId roomId
     * @return response(room)
     */
    @PutMapping("/{roomId}/join")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Room> joinRoom(@PathVariable String roomId) {

        log.trace("In RoomController joinRoom(roomId)");
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            log.trace("In if block: room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: add user");
                log.info("Check if user is already in room");
                if (room.getConnectedUsers().contains(name)){
                    log.trace("In if block: user is already in room");
                    return ResponseEntity.ok(room);
                }
                log.info("Check if the room is not started yet");
                if (!room.getStartedRoom()) {
                    log.trace("In if block: is not in room and a game has not started yet");
                    log.info("User is added to connected users list");
                    Room result = roomService.updateConnectedUsers(roomId, name);
                    log.info("Connected users list is updated successfully");
                    return ResponseEntity.ok(result);
                }
                else {
                    log.trace("In else block");
                    log.error("The user is already in room or the game has already started");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("Adding user is forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else {
            log.trace("In else block: the room is null");
            log.error("The room is null");
            throw new EntityNotFoundException("The room with the given id was not found");
        }
    }

    /**
     * Leave specific room
     * @param roomId roomId
     * @return reponce room
     */
    //TODO: join-t le kene cserelne leave-re
    @DeleteMapping("/{roomId}/join")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Room> leaveRoom(@PathVariable String roomId) {
        log.trace("In RoomController leaveRoom(roomId) method");
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            log.trace("In if block: the room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: delete user");
                if (room.getConnectedUsers().contains(name)) {
                    log.trace("In if block: the room contains user");
                    Room result = roomService.deleteConnectedUsers(roomId, name);
                    log.info("The user removal was successful");
                    return ResponseEntity.ok(result);
                }
                else {
                    log.trace("In else block: the user is not in the room");
                    log.error("The removal is forbidden");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("The removal is forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else {
            log.trace("In else block: the room is null");
            log.error("The removal is forbidden");
            throw new EntityNotFoundException("The room with the given id was not found");
        }
    }

    /**
     * Kick player out from room
     * @param roomId roomId
     * @param playerName username
     * @return response room
     */
    @DeleteMapping("/{roomId}/kick/{playerName}")
    @Secured(User.ROLE_CZAR)
    public ResponseEntity<Room> kickPlayer(@PathVariable String roomId, @PathVariable String playerName) {
        log.trace("In RoomController kickPlayer(roomId,playerName)");
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            log.trace("In if block: the room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: kicking out player");
                log.info("Checking if invoker is czar and a removable user is in the room");
                if (room.getCzarId().equals(name) && room.getConnectedUsers().contains(playerName)) {
                    log.trace("In if block: the invoker is czar and removable user is in the room");
                    Room result = roomService.deleteConnectedUsers(roomId, playerName);
                    log.info("The removal was successful");
                    return ResponseEntity.ok(result);
                }
                else {
                    log.trace("In else block: the invoker was not czar or the user is not in the room");
                    log.error("The removal was not successful");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("The removal was not successful");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else {
            log.trace("In else block: the room is null");
            log.error("The removal was not successful");
            throw new EntityNotFoundException("The room with the given id was not found");
        }
    }

    /**
     * Getting white cards from a specific room
     * @param roomId roomId
     * @return whiteList
     */
    @GetMapping("/{roomId}/whites/list")
    @Secured(User.ROLE_USER)
    public List<White> getWhiteCardsFromRoom(
            @PathVariable String roomId
    ) {
        log.trace("In RoomController getWhiteCardsFromRoom(roomId) method");
        Room room = roomService.getByRoomId(roomId);
        List<Integer> whiteIds = room.getWhiteIds();
        List<White> whites = new ArrayList<>();
        log.info("Get white cards from room");
        for (Integer id : whiteIds) {
            whites.add(roomService.getByWhiteId(id));
        }
        return whites;
    }

    /**
     * Get specific white card from a specific room
     * @param roomId roomId
     * @param id whiteId
     * @return response white card
     */
    @GetMapping("/{roomId}/whites/{id}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<White> getWhiteCardFromRoomById(
            @PathVariable String roomId,
            @PathVariable int id
    ) {
        log.trace("In RoomController getWhiteCardFromRoomById(roomId,id) method");
        Room room = roomService.getByRoomId(roomId);
        int whiteId = room.getWhiteIds().get(id);
        White white = roomService.getByWhiteId(whiteId);
        if (white == null) {
            log.trace("In if block: white is null");
            log.error("White card with given id was not found");
            return ResponseEntity.notFound().build();
        } else {
            log.trace("In else block: white is not null");
            log.info("White card with given id is found");
            return ResponseEntity.ok(white);
        }
    }

    /**
     * Getting black cards from a specific room
     * @param roomId roomId
     * @return blackList
     */
    @GetMapping("/{roomId}/blacks/list")
    @Secured(User.ROLE_USER)
    public List<Black> getBlackCardsFromRoom(
            @PathVariable String roomId
    ) {
        log.trace("In RoomController getBlackCardsFromRoom(roomId)");
        Room room = roomService.getByRoomId(roomId);
        List<Integer> blackIds = room.getBlackIds();
        List<Black> blacks = new ArrayList<>();
        log.info("Get black cards from room");
        for (Integer id : blackIds) {
            blacks.add(roomService.getByBlackId(id));
        }
        return blacks;
    }

    /**
     * Get specific black card from a specific room
     * @param roomId roomId
     * @param id blackId
     * @return response black card
     */
    @GetMapping("/{roomId}/blacks/{id}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Black> getBlackCardFromRoomById(
            @PathVariable String roomId,
            @PathVariable int id
    ) {
        log.trace("In RoomController getBlackCardFromRoomById(roomId,id) method");
        Room room = roomService.getByRoomId(roomId);
        int blackId = room.getBlackIds().get(id);
        Black black = roomService.getByBlackId(blackId);
        if (black == null) {
            log.trace("In if block: black is null");
            log.error("Black card with given id was not found");
            return ResponseEntity.notFound().build();
        } else {
            log.trace("In else block: black card is not null");
            log.info("Black card with given id is found");
            return ResponseEntity.ok(black);
        }
    }

    /**
     * Get connected users from room
     * @param roomId roomId
     * @return users
     */
    @GetMapping("/{roomId}/connected_users/list")
    @Secured({User.ROLE_USER})
    public List<User> getConnectedUsersFromRoom(
            @PathVariable String roomId
    ) {
        log.trace("In RoomController getConnectedUsersFromRoom(roomId)");
        Room room = roomService.getByRoomId(roomId);
        List<String> userIds = room.getConnectedUsers();
        List<User> users = roomService.findAllByUserIds(userIds);
        log.info("All users was found");
        return users;
    }

    /**
     * Initializes the game in a specific room
     * @param roomId roomId
     * @param room_body room
     * @return room
     */
    @PutMapping("/{roomId}/initGame")
    @Secured(User.ROLE_CZAR)
    public ResponseEntity<Room> initGame(@PathVariable String roomId, @RequestBody Room room_body) {
        log.trace("In RoomController initGame(roomId, room_body) method");
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            log.trace("In if block: room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: initializing the game");
                if (room.getCzarId().equals(name) && !room.getStartedRoom()) {
                    log.trace("In if block: The invoker is czar and the game has not started yet");
                    room.setRounds(room_body.getRounds());
                    log.info("Set the rounds");
                    Room result = roomService.initGame(roomId);
                    log.info("Initializing the game was successful.");
                    return ResponseEntity.ok(result);
                }
                else {
                    log.trace("In else block: The invoker is not czar or the game has started");
                    log.error("The initialization is forbidden");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("The initialization is forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else  {
            log.trace("In else block: the room is null");
            log.error("The initialization is forbidden");
            throw new EntityNotFoundException("The room with the given id was not found");
        }
    }

    /**
     * Get game state of a specific room
     * @param roomId roomId
     * @return response game state
     */
    @GetMapping("/{roomId}/gameState")
    @Secured(User.ROLE_USER)
    public ResponseEntity<GameState> getGameState(@PathVariable String roomId) {
        log.trace("In RoomController getGameState(roomId)");
        Room room = roomService.getByRoomId(roomId);
        GameState gs = new GameState();
        if (room != null) {
            log.trace("In else block: the room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: getting game state");
                if (room.getConnectedUsers().contains(name) && room.getStartedRoom()) {
                    log.trace("In if block: the invoker is in the room and the game is started");
                    gs = roomService.getGameState(room, name);
                    log.info("The game state is returned");
                    return ResponseEntity.ok(gs);
                }
                else {
                    log.trace("In else block: the invoker is not in the room or the game has not started yet");
                    log.error("The method is forbidden");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gs);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("The method is forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gs);
            }
        } else {
            log.trace("In else block: the room is null");
            log.error("The method is forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gs);
        }
    }

    /**
     * Update choose cards map in a specific room
     * @param roomId roomId
     * @param map choose map
     * @return response room
     */
    @PutMapping("/{roomId}/chooseCards")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Room> chooseCards(@PathVariable String roomId, @RequestBody Map<String, List<Integer>> map) {
        log.trace("In RoomController chooseCards(roomId, map) method");
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            log.trace("In if block: room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: choose card");
                if (room.getConnectedUsers().contains(name) && room.getTurnState().equals(TURN_CHOOSING_CARDS)) {
                    log.trace("In if block: the invoker is in the room and and game state is choosing cards");
                    List<Integer> availableCards = room.getUserWhiteIds(name);
                    if (!map.containsKey("cards")) {
                        log.trace("In if block: the the map does not contain the key: \"cards\"");
                        log.error("Choosing is forbidden");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                    }
                    List<Integer> pickedCards = map.get("cards");
                    if (pickedCards.size() != roomService.getByBlackId(room.BlackId()).getPick()) {
                        log.trace("In if block: the size of the picked cards does not equal to pick defined by black card");
                        log.error("Choosing is forbidden");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                    }
                    for (int cardId: pickedCards)
                    {
                        if (!availableCards.contains(cardId)) {
                            log.trace("In for cycle -> In if block: the available cards does not contain the chosen card");
                            log.error("Choosing is forbidden");
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                        }
                    }

                    if (room.getUserChosen().containsKey(name + "_1")) {
                        log.trace("In if block: the chosen cards list contains the given key");
                        log.error("Choosing is forbidden");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                    }
                    Map<String, Integer> chosen = room.getUserChosen();
                    log.info("Initialize chosen cards for user");
                    for (int i = 0; i < pickedCards.size(); i++)
                    {
                        chosen.put(name + "_" + (i + 1), pickedCards.get(i));
                    }
                    log.info("Set chosen cards for user");
                    room.setUserChosen(chosen);
                    if (room.getUserChosen().size() >= roomService.getByBlackId(room.BlackId()).getPick() * room.getConnectedUsers().size()) {
                        log.trace("In if block: chosen cards is more all equals with needed black cards");
                        room.setTurnState(TURN_VOTING);
                    }
                    Room updatedRoom = roomService.save(room);
                    log.info("The room is updated");
                    return ResponseEntity.ok(updatedRoom);
                }
                else {
                    log.trace("In else block: the invoker is not in the room or them game state is not choosing cards");
                    log.error("The update is forbidden");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("The update is forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else {
            log.trace("In else block: the room is null");
            log.error("The room is null");
            throw new EntityNotFoundException("The room with the given id was not found");
        }
    }

    /**
     * Update vote map in specific room
     * @param roomId roomId
     * @param map vote map
     * @return response room
     */
    @PutMapping("/{roomId}/voteCards")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Room> voteCards(@PathVariable String roomId, @RequestBody Map<String, String> map) {
        log.trace("In RoomController voteCards(roomId,map) method");
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            log.trace("In if block: room is not null");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                log.trace("In try block: voting for cards");
                if (room.getConnectedUsers().contains(name) && room.getTurnState().equals(TURN_VOTING)) {
                    log.trace("In if branch: The invoker is in the room and the rooms state is voting");
                    if (!map.containsKey("user")) {
                        log.trace("If block: the map does not contains the key: \"user\"");
                        log.error("The voting for this user is forbidden");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                    }
                    String votedUser = map.get("user");
                    if (!room.getConnectedUsers().contains(votedUser)) {
                        log.trace("In if block: the user is not in the room");
                        log.error("The voting for this user is forbidden");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                    }


                    Map<String,String> votesMap = room.getUserVotes();
                    if (votesMap.containsKey(name)) {
                        log.trace("In if block: the user has already voted");
                        log.error("The voting for this user is forbidden");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                    }
                    votesMap.put(name, votedUser);
                    room.setUserVotes(votesMap);
                    if (votesMap.size() >= room.getConnectedUsers().size()) {
                        log.trace("The num of the vote is more or equals than the rooms in the game");
                        log.info("The votes map is updated");
                        roomService.updateVotes(room);
                        int maxRounds = room.getRounds();
                        int currentRound = room.getCurrentRound();
                        if (currentRound + 1 > maxRounds) {
                            log.trace("In if block: the next round is more than the predefined max round");
                            log.info("End Game");
                            room.setTurnState(TURN_END_GAME);
                        } else {
                            log.trace("In else block: the next round is less than the predefined max round");
                            log.info("Increase round");
                            room.setCurrentRound(currentRound + 1);
                            log.info("Change game state to choosing");
                            room.setTurnState(TURN_CHOOSING_CARDS);

                            log.info("Reseting user votes");
                            room.setUserVotes(new HashMap<String, String>());
                            log.info("Reseting user chosen cards");
                            room.setUserChosen(new HashMap<String,Integer>());
                        }
                    }

                    Room updatedRoom = roomService.save(room);
                    log.info("The room is updated");
                    return ResponseEntity.ok(updatedRoom);
                }
                else {
                    log.trace("In else block: The invoker is not in the room and the rooms state is not voting");
                    log.error("The voting for this user is forbidden");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
                }
            } catch (NonTransientDataAccessException e) {
                log.trace("In catch block");
                log.error("The voting for this user is forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else {
            log.trace("In else block: The room is null");
            log.error("The voting is not possible");
            throw new EntityNotFoundException("The room with the given id was not found");
        }
    }
}
