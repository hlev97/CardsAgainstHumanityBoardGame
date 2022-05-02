package hu.bme.cah.api.cardsagaintshumanityapi.card.controller;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.BlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/black")
public class BlackController {
    @Autowired
    private BlackService blackService;

    @GetMapping("/list")
    public Iterable<Black> list() {
        return blackService.list();
    }

    @GetMapping("/{blackId}")
    public ResponseEntity<Black> getByBlackId(@PathVariable long blackId) {
        Black black = blackService.getByBlackId(blackId);
        if (black == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(black);
    }

    @PostMapping
    public Black create(@RequestBody Black black) {
        black.setBlackId(null);
        return blackService.save(black);
    }

    @PutMapping("/{blackId}")
    public ResponseEntity<Black> update(@PathVariable long blackId, @RequestBody Black black) {
        try {
            Black result = blackService.update(blackId, black);
            return ResponseEntity.ok(result);
        } catch(NonTransientDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(black);
        }
    }

    @DeleteMapping("/{blackId}")
    public ResponseEntity<?> delete(@PathVariable long blackId) {
        Black black = blackService.getByBlackId(blackId);
        if (black == null) {
            return ResponseEntity.notFound().build();
        } else {
            blackService.delete(blackId);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/random/{num}")
    public List<Black> getRandomOfBlacks(@PathVariable int num) {
        return blackService.getRandomOfBlacks(num);
    }
}
