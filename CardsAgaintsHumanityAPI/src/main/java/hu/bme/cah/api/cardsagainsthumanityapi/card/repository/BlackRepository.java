package hu.bme.cah.api.cardsagainsthumanityapi.card.repository;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Repository of CRUD operations
 */
@Repository
@Slf4j
public class BlackRepository {
    /**
     * Injection of {@link EntityManager} to access database
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Save {@link Black} card to database
     * @param black
     * @return {@link Black}
     */
    @Transactional
    public Black save(Black black) {
        log.trace("BlackRepository save method is accessed");
        Black result = em.merge(black);
        log.info("The black card was saved successfully");
        return result;
    }

    /**
     * SaveAll given black cards
     * @param blacks
     */
    @Transactional
    public void saveAll(List<Black> blacks) {
        log.trace("BlackRepository saveAll(blacks) method is accessed");
        for (Black black :  blacks) {
            log.info(String.format("Text: %s, Pick: %d, Pack: %s", black.getText().replace('\n', ' '), black.getPick(), black.getPack()));
            save(black);
        }
        log.info("Saving of the given black cards was successful");
    }

    /**
     * Find black card with given id
     * @param id
     * @return {@link Black}
     */
    public Black findByBlackId(long id) {
        log.trace("BlackRepository findByBlackId(id) method is accessed");
        String getByIdQuery = "SELECT b FROM Black b WHERE b.blackId=:id";
        TypedQuery<Black> query = em.createQuery(getByIdQuery, Black.class).setParameter("id", id);
        Black result = query.getSingleResult();
        log.info("The card with the certain id is found");
        return result;
    }

    /**
     * Find all black cards
     * @return {@link List<Black>}
     */
    public List<Black> findAll() {
        log.trace("BlackRepository findAll() method is accessed");
        List<Black> results = em.createQuery("SELECT b FROM Black b", Black.class).getResultList();
        log.info("Query ran successfully");
        return results;
    }

    /**
     * Update a certain black card with given content
     * @param blackId
     * @param black
     * @return {@link Black}
     */
    @Transactional
    public Black update(long blackId, Black black) {
        log.trace("BlackRepository update(blackId, black) method is accessed");
        Black result = findByBlackId(blackId);
        if (result == null) {
            log.trace("In if block: result is null");
            log.error("The card with the certain id wasn't found");
            throw new EntityNotFoundException();
        } else {
            log.trace("In else block: result is not null");
            log.info("Update card with new content");
            result.setText(black.getText());
            result.setPack(black.getPack());
            result.setPick(black.getPick());
            Black updatedCard = em.merge(result);
            if (updatedCard == null) {
                log.error("Updating the card was unsuccessful");
            } else {
                log.info("Updating the card was successful");
            }
            return updatedCard;
        }
    }

    /**
     * Delete black card with given id
     * @param id
     */
    @Transactional
    public void deleteById(long id) {
        log.trace("BlackRepository deleteById(id) method is accessed");
        Black black = findByBlackId(id);
        if (black == null) {
            log.trace("In if block: black is null");
            log.error("The card with the certain id wasn't found");
            throw new EntityNotFoundException();
        } {
            log.trace("In else block: black is not null");
            em.remove(black);
            log.info("The deletion was successful.");
        }
    }
}