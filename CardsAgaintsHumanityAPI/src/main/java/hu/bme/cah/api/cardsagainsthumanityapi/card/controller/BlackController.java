package hu.bme.cah.api.cardsagainsthumanityapi.card.controller;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.service.BlackService;
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

import java.util.List;
import java.util.Objects;

/**
 * RestController to give access through {@link BlackService} to {@link Black} cards
 */
@RestController
@RequestMapping("/api/black")
@Slf4j
public class BlackController {
    /**
     * Injection of {@link BlackService}
     */
    @Autowired
    private BlackService blackService;

    /**
     * Getting all {@link Black} cards.
     * The invoker must have at least ROLE_USER authority to be able give this request.
     * @return {@link List<Black>}
     */
    @GetMapping("/list")
    @Secured(User.ROLE_USER)
    public List<Black> list() {
        log.trace("BlackController list() method accessed");
        log.info("Getting all black cards...");
        return blackService.list();
    }

    /**
     * Getting a black card with certain id. The invoker must have at least ROLE_USER authority to be able give this request.
     * @param blackId
     * @return {@link Black}
     */
    @GetMapping("/{blackId}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Black> getByBlackId(@PathVariable long blackId) {
        log.trace("BlackController getByBlackId(blackId) method accessed");
        log.info("Getting specific black card with id...");
        Black black = blackService.getByBlackId(blackId);
        if (black == null) {
            log.trace("if branch is accessed in getBlackById: black is null");
            log.error("The card with the certain id does not listed in the database...");
            return ResponseEntity.notFound().build();
        } else {
            log.trace("else branch us accessed in getBlackById: black is not null");
            log.info("The card with the certain id is found in the database...");
            return ResponseEntity.ok(black);
        }
    }

    /**
     * Adding a new card to the the database. The User must have at least ROLE_USER authority to be able to do this request.
     * @param black
     * @return {@link Black}
     */
    @PostMapping
    @Secured({User.ROLE_USER, User.ROLE_ADMIN})
    public Black create(@RequestBody Black black) {
        log.trace("BlackController create(black) method accessed");
        log.info("Add new black card to database...");
        black.setBlackId(null);
        return blackService.save(black);
    }

    /**
     * Updating a card with given database and save changes. The User must have at least ROLE_USER authority to be able to do this request.
     * @param blackId
     * @param black
     * @return {@link ResponseEntity<Black>}
     */
    @PutMapping("/{blackId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<Black> update(@PathVariable long blackId, @RequestBody Black black) {
        log.trace("BlackController update(blackId,black) method accessed");
        log.info("Update black card with certain is...");
        log.warn("The invoker must have at least user role to do this operation");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            log.trace("In try block");
            log.info("Check if user has the required roles");
            if (Objects.equals(blackService.getByBlackId(blackId).getPack(), "Base")) {
                log.trace("In if block: the invoker tries to update a base pack card");
                log.warn("The invoker must be an admin to modify base cards");
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    log.trace("In if block: ");
                    log.error("The user does not have authority to modify base cards");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    log.trace("In else block:");
                    log.info("The authentication was successful");
                    Black result = blackService.update(blackId, black);
                    return ResponseEntity.ok(result);
                }
            } else {
                log.trace("In else block: the invoker tries to update a custom a card");
                log.info("The authentication was successful");
                Black result = blackService.update(blackId, black);
                return ResponseEntity.ok(result);
            }
        } catch(NonTransientDataAccessException e) {
            log.trace("In check block");
            log.error("The modification is forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(black);
        }
    }

    /**
     * Deleting a card with given id from database. The User must have at least ROLE_USER authority to be able to do this request.
     * @param blackId
     * @return {@link ResponseEntity<?>}
     */
    @DeleteMapping("/{blackId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<?> delete(@PathVariable long blackId) {
        log.trace("BlackController delete(blackId) method accessed");
        log.info("Delete black card with certain id");
        log.warn("The invoker must have at least user role to do this operation");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Black black = blackService.getByBlackId(blackId);
        try {
            log.info("Check if user has the required authority to delete card");
            if (Objects.equals(blackService.getByBlackId(blackId).getPack(), "Base")) {
                log.warn("The invoker must be an admin to delete base cards");
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    log.error("The user is not authenticated to delete this card");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    log.info("The invoker is able to delete this card");
                    blackService.delete(blackId);
                    return ResponseEntity.ok().build();
                }
            } else {
                blackService.delete(blackId);
                log.info("The deletion was successful");
                return ResponseEntity.ok().build();
            }
        } catch (NonTransientDataAccessException e) {
            log.trace("In check block");
            log.error("The deletion is forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(black);
        }
    }
}
