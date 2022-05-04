package hu.bme.cah.api.cardsagaintshumanityapi.card.repository;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class WhiteRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public White save(White white) { return em.merge(white); }

    @Transactional
    public void saveAll(List<White> whites) {
        for (White white :  whites) {
            save(white);
        }
    }

    public White findByWhiteId(long id) {
        return em.find(White.class, id);
    }

    public List<White> findAll() {
        return em.createQuery("SELECT w FROM White w", White.class).getResultList();
    }

    @Transactional
    public White update(long whiteId, White white) {
        White result = findByWhiteId(whiteId);
        if (result == null) throw new EntityNotFoundException();
        else {
            result.setText(white.getText());
            result.setPack(white.getPack());
            return em.merge(result);
        }
    }

    @Transactional
    public void deleteById(long id) {
        White white = findByWhiteId(id);
        em.remove(white);
    }
}