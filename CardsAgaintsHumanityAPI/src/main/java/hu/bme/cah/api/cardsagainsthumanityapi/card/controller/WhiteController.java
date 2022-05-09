package hu.bme.cah.api.cardsagainsthumanityapi.card.controller;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagainsthumanityapi.card.service.WhiteService;
import hu.bme.cah.api.cardsagainsthumanityapi.log.domain.Log;
import hu.bme.cah.api.cardsagainsthumanityapi.log.domain.LogLevel;
import hu.bme.cah.api.cardsagainsthumanityapi.log.service.LogService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * RestController to give access through {@link WhiteService} to {@link White} cards
 */
@RestController
@RequestMapping("/api/white")
@Slf4j
public class WhiteController {
    /**
     * Injection of {@link WhiteService}
     */
    @Autowired
    private WhiteService whiteService;

    @Autowired
    private LogService logService;

    /**
     * Getting all {@link White} cards.
     * The invoker must have at least ROLE_USER authority to be able give this request.
     * @return {@link List<White>}
     */
    @GetMapping("/list")
    @Secured(User.ROLE_USER)
    public List<White> list() {
        log.trace("WhiteController list() method accessed");
        String text = "Getting all white cards from database...";
        Log loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
        logService.save(loggedEvent);
        log.info(text);
        return whiteService.list();
    }

    /**
     * Getting a {@link White} card with certain id. The invoker must have at least ROLE_USER authority to be able give this request.
     * @param whiteId
     * @return {@link White}
     */
    @GetMapping("/{whiteId}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<White> getByWhiteId(@PathVariable long whiteId) {
        log.trace("WhiteController getWhiteById(whiteId) method accessed");
        String text = "Getting white card by id";
        Log loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
        logService.save(loggedEvent);
        log.info(text);
        White white = whiteService.getByWhiteId(whiteId);
        if (white == null) {
            log.trace("In if block:");
            text = "Card with the certain id was not found";
            loggedEvent = new Log(null, text, LogLevel.ERROR, LocalDateTime.now());
            logService.save(loggedEvent);
            log.error(text);
            return ResponseEntity.notFound().build();
        } else {
            text = "The card was found successfully";
            loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
            logService.save(loggedEvent);
            log.info(text);
            return ResponseEntity.ok(white);
        }
    }

    /**
     * Adding a new card to the the database. The User must have at least ROLE_USER authority to be able to do this request.
     * @param white
     * @return {@link White}
     */
    @PostMapping
    @Secured({User.ROLE_USER, User.ROLE_ADMIN})
    public White create(@RequestBody White white) {
        String text = "Adding new white card...";
        Log loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
        logService.save(loggedEvent);
        log.info(text);
        white.setWhiteId(null);
        return whiteService.save(white);
    }

    /**
     * Updating a card with given database and save changes. The User must have at least ROLE_USER authority to be able to do this request.
     * @param whiteId
     * @param white
     * @return {@link ResponseEntity<White>}
     */
    @PutMapping("/{whiteId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<White> update(@PathVariable long whiteId, @RequestBody White white) {
        String text = "Update method accessed.";
        Log loggedEvent = new Log(null, text, LogLevel.TRACE, LocalDateTime.now());
        logService.save(loggedEvent);
        log.trace(text);

        text = "Updating white card with certain id.";
        loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
        logService.save(loggedEvent);
        log.info(text);

        text = "The invoker must have at least user role to do this operation";
        loggedEvent = new Log(null, text, LogLevel.WARN, LocalDateTime.now());
        logService.save(loggedEvent);
        log.warn(text);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (Objects.equals(whiteService.getByWhiteId(whiteId).getPack(), "Base")) {
                text = "The invoker must be an admin to modify base cards";
                loggedEvent = new Log(null, text, LogLevel.WARN, LocalDateTime.now());
                logService.save(loggedEvent);
                log.warn(text);
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    text = "The user is not authenticated to do this operation";
                    loggedEvent = new Log(null, text, LogLevel.ERROR, LocalDateTime.now());
                    logService.save(loggedEvent);
                    log.error(text);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    text = "The invoker is able to do the modification";
                    loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
                    logService.save(loggedEvent);
                    log.info(text);
                    White result = whiteService.update(whiteId, white);
                    return ResponseEntity.ok(result);
                }
            } else {
                White result = whiteService.update(whiteId, white);
                text = "The invoker is able to do the modification";
                loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
                logService.save(loggedEvent);
                log.info(text);
                return ResponseEntity.ok(result);
            }
        } catch(NonTransientDataAccessException e) {
            text = "The user is not authenticated to do this operation";
            loggedEvent = new Log(null, text, LogLevel.ERROR, LocalDateTime.now());
            logService.save(loggedEvent);
            log.error(text);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(white);
        }
    }

    /**
     * Deleting a card with given id from database. The User must have at least ROLE_USER authority to be able to do this request.
     * @param whiteId
     * @return {@link ResponseEntity<?>}
     */
    @DeleteMapping("/{whiteId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<?> delete(@PathVariable long whiteId) {
        String text = "Deleting white card with given id...";
        Log loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
        logService.save(loggedEvent);
        log.info(text);
        text = "The invoker must have at least user role to do this operation";
        loggedEvent = new Log(null, text, LogLevel.WARN, LocalDateTime.now());
        logService.save(loggedEvent);
        log.warn(text);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        White white = whiteService.getByWhiteId(whiteId);
        if (white == null) {
            text = "The card with the given id was not found";
            loggedEvent = new Log(null, text, LogLevel.ERROR, LocalDateTime.now());
            logService.save(loggedEvent);
            log.error(text);
            return ResponseEntity.notFound().build();
        } else {
            if (Objects.equals(whiteService.getByWhiteId(whiteId).getPack(), "Base")) {
                text = "The invoker must be an admin to delete base cards";
                loggedEvent = new Log(null, text, LogLevel.WARN, LocalDateTime.now());
                logService.save(loggedEvent);
                log.warn(text);
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    text = "The user is not authenticated to delete this card";
                    loggedEvent = new Log(null, text, LogLevel.ERROR, LocalDateTime.now());
                    logService.save(loggedEvent);
                    log.error(text);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    text = "The invoker is able to delete this card";
                    loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
                    logService.save(loggedEvent);
                    log.info(text);
                    whiteService.delete(whiteId);
                    text = "The deletion was successful";
                    loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
                    logService.save(loggedEvent);
                    log.info(text);
                    return ResponseEntity.ok().build();
                }
            } else {
                whiteService.delete(whiteId);
                text = "The deletion was successful";
                loggedEvent = new Log(null, text, LogLevel.INFO, LocalDateTime.now());
                logService.save(loggedEvent);
                log.info(text);
                return ResponseEntity.ok().build();
            }
        }
    }
}
