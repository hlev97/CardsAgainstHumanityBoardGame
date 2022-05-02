package hu.bme.cah.api.cardsagaintshumanityapi.card.controller;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.WhiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/white")
public class WhiteController {
    @Autowired
    private WhiteService whiteService;

    @GetMapping("/list")
    public List<White> list() {
        return whiteService.list();
    }

    @GetMapping("/{whiteId}")
    public ResponseEntity<White> getByWhiteId(@PathVariable long whiteId) {
        White white = whiteService.getByWhiteId(whiteId);
        if (white == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(white);
    }

    @PostMapping
    public White create(@RequestBody White white) {
        white.setWhiteId(null);
        return whiteService.save(white);
    }

    @PutMapping("/{whiteId}")
    public ResponseEntity<White> update(@PathVariable long whiteId, @RequestBody White white) {
        try {
            White result = whiteService.update(whiteId, white);
            return ResponseEntity.ok(result);
        } catch(NonTransientDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(white);
        }
    }

    @DeleteMapping("/{whiteId}")
    public ResponseEntity<?> delete(@PathVariable long whiteId) {
        White white = whiteService.getByWhiteId(whiteId);
        if (white == null) {
            return ResponseEntity.notFound().build();
        } else {
            whiteService.delete(whiteId);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/random/{num}")
    public List<White> getRandomOfWhites(@PathVariable int num) {
        return whiteService.getRandomOfWhites(num);
    }
}
