package hu.bme.cah.api.cardsagaintshumanityapi.card.controller;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.BlackService;
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
@RequestMapping("/api/black")
public class BlackController {
    @Autowired
    private BlackService blackService;

    @GetMapping("/list")
    @Secured(User.ROLE_USER)
    public List<Black> list() {
        return blackService.list();
    }

    @GetMapping("/{blackId}")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Black> getByBlackId(@PathVariable long blackId) {
        Black black = blackService.getByBlackId(blackId);
        if (black == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(black);
    }

    @PostMapping
    @Secured({User.ROLE_USER, User.ROLE_ADMIN})
    public Black create(@RequestBody Black black) {
        black.setBlackId(null);
        return blackService.save(black);
    }

    @PutMapping("/{blackId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<Black> update(@PathVariable long blackId, @RequestBody Black black) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (Objects.equals(blackService.getByBlackId(blackId).getPack(), "Base")) {
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    Black result = blackService.update(blackId, black);
                    return ResponseEntity.ok(result);
                }
            } else {
                Black result = blackService.update(blackId, black);
                return ResponseEntity.ok(result);
            }
        } catch(NonTransientDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(black);
        }
    }

    @DeleteMapping("/{blackId}")
    @Secured({User.ROLE_ADMIN, User.ROLE_USER})
    public ResponseEntity<?> delete(@PathVariable long blackId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Black black = blackService.getByBlackId(blackId);
        if (black == null) {
            return ResponseEntity.notFound().build();
        } else {
            if (Objects.equals(blackService.getByBlackId(blackId).getPack(), "Base")) {
                if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(User.ROLE_ADMIN))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } else {
                    blackService.delete(blackId);
                    return ResponseEntity.ok().build();
                }
            } else {
                blackService.delete(blackId);
                return ResponseEntity.ok().build();
            }
        }
    }
}
