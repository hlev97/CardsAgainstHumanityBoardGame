package hu.bme.cah.api.cardsagainsthumanityapi.card.service;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.repository.BlackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.bme.cah.api.cardsagainsthumanityapi.card.controller.BlackController;

import java.util.List;

/**
 * Ensures that {@link BlackController} can access {@link Black} through {@link BlackRepository}
 */
@Service
@Slf4j
public class BlackService {
    /**
     * Injection of {@link BlackRepository}
     */
    @Autowired
    private BlackRepository blackRepository;

    /**
     * Returns all {@link Black} cards in the database
     * @return {@link List<Black>}
     */
    public List<Black> list() {
        log.trace("BlackService list() method is accessed");
        return blackRepository.findAll();
    }

    /**
     * Save given black card to database
     * @param black
     * @return {@link Black}
     */
    public Black save(Black black) {
        log.trace("BlackService save(black) method is accessed");
        return blackRepository.save(black);
    }

    /**
     * Save given list of black cards to database
     * @param blacks
     */
    public void save(List<Black> blacks) {
        log.trace("BlackService save(blacks) method is accessed");
        blackRepository.saveAll(blacks);
    }

    /**
     * Get black card by id from database
     * @param blackId
     * @return {@link Black}
     */
    public Black getByBlackId(long blackId) {
        log.trace("BlackService getByBlackId(blackId) method is accessed");
        return blackRepository.findByBlackId(blackId);
    }

    /**
     * Update card with given id and content
     * @param blackId
     * @param black
     * @return {@link Black}
     */
    public Black update(long blackId, Black black) {
        log.trace("BlackService update(blackId, black) method is accessed");
        return blackRepository.update(blackId, black);
    }

    /**
     * Delete card with given id
     * @param blackId
     */
    public void delete(long blackId) {
        log.trace("BlackService delete(blackId) method is accessed");
        blackRepository.deleteById(blackId);
    }
}
