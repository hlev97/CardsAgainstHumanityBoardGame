package hu.bme.cah.api.cardsagainsthumanityapi.card.service;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagainsthumanityapi.card.repository.WhiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.bme.cah.api.cardsagainsthumanityapi.card.controller.WhiteController;
import java.util.List;

/**
 * Ensures that {@link WhiteController} can access {@link White} through {@link WhiteRepository}
 */
@Service
@Slf4j
public class WhiteService {
    /**
     * Injection of {@link WhiteRepository}
     */
    @Autowired
    private WhiteRepository whiteRepository;

    /**
     * Returns all {@link White} cards in the database
     * @return {@link List<White>}
     */
    public List<White> list() {
        log.trace("WhiteService list() method is accessed");
        return whiteRepository.findAll();
    }

    /**
     * Save given white card to database
     * @param white
     * @return {@link White}
     */
    public White save(White white) {
        log.trace("WhiteService save(white) method is accessed");
        return whiteRepository.save(white);
    }

    /**
     * Save given list of white cards to database
     * @param whites
     */
    public void save(List<White> whites) {
        log.trace("WhiteService save(whites) method is accessed");
        whiteRepository.saveAll(whites);
    }

    /**
     * Get white card by id from database
     * @param whiteId
     * @return {@link White}
     */
    public White getByWhiteId(long whiteId) {
        log.trace("WhiteService getByWhiteId(whiteId) method is accessed");
        return whiteRepository.findByWhiteId(whiteId);
    }

    /**
     * Update card with given id and content
     * @param whiteId
     * @param white
     * @return {@link White}
     */
    public White update(long whiteId, White white) {
        log.trace("WhiteService update(whiteId, white) method accessed");
        return whiteRepository.update(whiteId, white);
    }

    /**
     * Delete card with given id
     * @param whiteId
     */
    public void delete(long whiteId) {
        log.trace("WhiteService delete(whiteId) method accessed");
        whiteRepository.deleteById(whiteId);
    }
}
