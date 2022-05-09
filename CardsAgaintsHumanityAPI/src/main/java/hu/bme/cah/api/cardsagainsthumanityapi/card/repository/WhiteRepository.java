package hu.bme.cah.api.cardsagainsthumanityapi.card.repository;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;


/**
 * Repository of CRUD operations
 */
@Repository
@Slf4j
public class WhiteRepository {
    /**
     * Injection of {@link EntityManager} to access database
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Save {@link White} card to database
     * @param white
     * @return {@link White}
     */
    @Transactional
    public White save(White white) {
        log.trace("WhiteRepository save(white) method was accessed");
        White result = em.merge(white);
        log.info("The white card was saved successfully");
        return result;
    }

    /**
     * SaveAll given white cards
     * @param whites
     */
    @Transactional
    public void saveAll(List<White> whites) {
        log.trace("WhiteRepository saveAll(whites) method was accessed");
        for (White white :  whites) {
            log.info(String.format("Text: %s, Pack: %s", white.getText().replace('\n', ' '), white.getPack()));
            save(white);
        }
        log.info("Saving of the given white cards was successful");
    }

    /**
     * Find white card with given id
     * @param id
     * @return {@link White}
     */
    public White findByWhiteId(long id) {
        log.trace("WhiteRepository findByWhiteId(whiteId) method is accessed");
        String getByIdQuery = "SELECT w FROM White w WHERE w.whiteId=:id";
        TypedQuery<White> query = em.createQuery(getByIdQuery, White.class).setParameter("id", id);
        White result = query.getSingleResult();
        log.info("The card with the certain id is found");
        return result;
    }

    /**
     * Find all white cards
     * @return {@link List<White>}
     */
    public List<White> findAll() {
        log.trace("WhiteRepository findAll() method is accessed");
        List<White> results = em.createQuery("SELECT w FROM White w", White.class).getResultList();
        log.info("Query ran successfully");
        return results;
    }

    /**
     * Update a certain white card with given content
     * @param whiteId
     * @param white
     * @return {@link White}
     */
    @Transactional
    public White update(long whiteId, White white) {
        log.trace("WhiteRepository update(whiteId, white) method is accessed");
        White result = findByWhiteId(whiteId);
        if (result == null) {
            log.trace("In if block: result is null");
            log.error("The card with the certain id wasn't found");
            throw new EntityNotFoundException();
        } else {
            log.trace("In else block: result is not null");
            log.info("Update card with new content");
            result.setText(white.getText());
            result.setPack(white.getPack());
            White updatedCard = em.merge(result);
            if (updatedCard == null) {
                log.error("Updating the card was unsuccessful");
            } else {
                log.info("Updating the card was successful");
            }
            return updatedCard;
        }
    }

    /**
     * Delete white card with given id
     * @param id
     */
    @Transactional
    public void deleteById(long id) {
        log.trace("WhiteRepository deleteById(id) method is accessed");
        White white = findByWhiteId(id);
        if (white != null) {
            log.trace("In if block: white is not null");
            log.info("The card with the certain id was found");
            em.remove(white);
        } else {
            log.trace("In else block: white is null");
            log.error("The card with the certain id wasn't found");
        }
    }
}