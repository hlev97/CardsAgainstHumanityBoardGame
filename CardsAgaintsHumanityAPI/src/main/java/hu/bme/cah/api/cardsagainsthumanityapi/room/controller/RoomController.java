package hu.bme.cah.api.cardsagainsthumanityapi.room.controller;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagainsthumanityapi.email.service.EmailService;
import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.Room;
import hu.bme.cah.api.cardsagainsthumanityapi.room.service.RoomService;
import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @Autowired
    EmailService emailService;

    @PostMapping
    @Secured(User.ROLE_USER)
    public Room create(@RequestBody Room room) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        roomService.updateUserRoleById(auth.getName(), User.ROLE_CZAR);
        Room result = roomService.createRoom(room, auth.getName());
        List<String> userIds = roomService.getByRoomId(result.getRoomId()).getAllowedUsers();
        List<User> users = roomService.findAllByUserIds(userIds);
        sendEmails(users);
        return result;
    }

    private void sendEmails(List<User> recipients) {
        for (User to : recipients) {
            emailService.sendEmail(
                    to.getEmail(),
                    "test",
                    "test"
            );
        }
    }

    @GetMapping("/{roomId}")
    @Secured(User.ROLE_USER)
    public Room getByRoomId(@PathVariable long roomId) {
        return roomService.getByRoomId(roomId);
    }

    @GetMapping("/list")
    @Secured(User.ROLE_USER)
    public List<Room> list() {
        return roomService.list();
    }

    @DeleteMapping("/{roomId}")
    @Secured({User.ROLE_CZAR, User.ROLE_ADMIN})
    public ResponseEntity<?> delete(@PathVariable long roomId) {
        Room room = roomService.getByRoomId(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        } else {
            roomService.removeByRoomId(roomId);
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/{roomId}/join")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Room> joinRoom(@PathVariable long roomId) {
        Room room = roomService.getByRoomId(roomId);
        if (room != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            try {
                if (room.getAllowedUsers().contains(name)) {
                    Room result = roomService.updateConnectedUsers(roomId, name);
                    return ResponseEntity.ok(result);
                } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            } catch (NonTransientDataAccessException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
            }
        } else  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(room);
    }

    @GetMapping("/{roomId}/whites/list")
    @Secured(User.ROLE_USER)
    public List<White> getWhiteCardsFromRoom(
            @PathVariable long roomId
    ) {
        Room room = roomService.getByRoomId(roomId);
        List<Integer> whiteIds = room.getWhiteIds();
        List<White> whites = new ArrayList<>();
        for (Integer id : whiteIds) {
            whites.add(roomService.getByWhiteId(id));
        }
        return whites;
    }

    @GetMapping("/{roomId}/whites/{id}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<White> getWhiteCardFromRoomById(
            @PathVariable long roomId,
            @PathVariable int id
    ) {
        Room room = roomService.getByRoomId(roomId);
        int whiteId = room.getWhiteIds().get(id);
        White white = roomService.getByWhiteId(whiteId);
        if (white == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(white);
    }

    @GetMapping("/{roomId}/blacks/list")
    @Secured(User.ROLE_USER)
    public List<Black> getBlackCardsFromRoom(
            @PathVariable long roomId
    ) {
        Room room = roomService.getByRoomId(roomId);
        List<Integer> blackIds = room.getBlackIds();
        List<Black> blacks = new ArrayList<>();
        for (Integer id : blackIds) {
            blacks.add(roomService.getByBlackId(id));
        }
        return blacks;
    }

    @GetMapping("/{roomId}/blacks/{id}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Black> getBlackCardFromRoomById(
            @PathVariable long roomId,
            @PathVariable int id
    ) {
        Room room = roomService.getByRoomId(roomId);
        int blackId = room.getBlackIds().get(id);
        Black black = roomService.getByBlackId(blackId);
        if (black == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(black);
    }

    @GetMapping("/{roomId}/allowed_users/list")
    @Secured({User.ROLE_CZAR, User.ROLE_ADMIN})
    public List<User> getUsersFromRoom(
            @PathVariable long roomId
    ) {
        Room room = roomService.getByRoomId(roomId);
        List<String> userIds = room.getAllowedUsers();
        return roomService.findAllByUserIds(userIds);
    }

    @GetMapping("/{roomId}/connected_users/list")
    @Secured({User.ROLE_USER})
    public List<User> getConnectedUsersFromRoom(
            @PathVariable long roomId
    ) {
        Room room = roomService.getByRoomId(roomId);
        List<String> userIds = room.getConnectedUsers();
        return roomService.findAllByUserIds(userIds);
    }

    @PutMapping("/{roomId}/initGame")
    @Secured(User.ROLE_CZAR)
    public ResponseEntity<Room> initGame(@PathVariable long roomId) {

        return ResponseEntity.ok().build();
    }
}
