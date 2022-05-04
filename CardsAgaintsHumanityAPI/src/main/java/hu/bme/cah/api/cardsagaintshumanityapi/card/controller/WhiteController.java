package hu.bme.cah.api.cardsagaintshumanityapi.card.controller;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.WhiteService;
import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
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

@RestController
@RequestMapping("/api/white")
public class WhiteController {
    @Autowired
    private WhiteService whiteService;

    @GetMapping("/list")
    @Secured(User.ROLE_USER)
    public List<White> list() {
        return whiteService.list();
    }

    @GetMapping("/{whiteId}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<White> getByWhiteId(@PathVariable long whiteId) {
        White white = whiteService.getByWhiteId(whiteId);
        if (white == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(white);
    }

    @PostMapping
    @Secured({User.ROLE_USER, User.ROLE_ADMIN})
    public White create(@RequestBody White white) {
        white.setWhiteId(null);
        return whiteService.save(white);
    }

    @PutMapping("/{whiteId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<White> update(@PathVariable long whiteId, @RequestBody White white) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (Objects.equals(whiteService.getByWhiteId(whiteId).getPack(), "Base")) {
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    White result = whiteService.update(whiteId, white);
                    return ResponseEntity.ok(result);
                }
            } else {
                White result = whiteService.update(whiteId, white);
                return ResponseEntity.ok(result);
            }
        } catch(NonTransientDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(white);
        }
    }

    @DeleteMapping("/{whiteId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<?> delete(@PathVariable long whiteId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        White white = whiteService.getByWhiteId(whiteId);
        if (white == null) {
            return ResponseEntity.notFound().build();
        } else {
            if (Objects.equals(whiteService.getByWhiteId(whiteId).getPack(), "Base")) {
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    whiteService.delete(whiteId);
                    return ResponseEntity.ok().build();
                }
            } else {
                whiteService.delete(whiteId);
                return ResponseEntity.ok().build();
            }
        }
    }
}
