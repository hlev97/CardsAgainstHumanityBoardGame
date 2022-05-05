package hu.bme.cah.api.cardsagaintshumanityapi.card.repository;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BlackRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Black save(Black black) { return em.merge(black); }

    @Transactional
    public void saveAll(List<Black> blacks) {
        for (Black black :  blacks) {
            save(black);
        }
    }

    public Black findByBlackId(long id) {
        String getByIdQuery = "SELECT b FROM Black b WHERE b.blackId=:id";
        TypedQuery<Black> query = em.createQuery(getByIdQuery, Black.class).setParameter("id", id);
        return query.getSingleResult();
    }

    public List<Black> findAll() {
        return em.createQuery("SELECT b FROM Black b", Black.class).getResultList();
    }

    @Transactional
    public Black update(long blackId, Black black) {
        Black result = findByBlackId(blackId);
        if (result == null) throw new EntityNotFoundException();
        else {
            result.setText(black.getText());
            result.setPack(black.getPack());
            result.setPick(black.getPick());
            return em.merge(result);
        }
    }

    @Transactional
    public void deleteById(long id) {
        Black black = findByBlackId(id);
        em.remove(black);
    }
}