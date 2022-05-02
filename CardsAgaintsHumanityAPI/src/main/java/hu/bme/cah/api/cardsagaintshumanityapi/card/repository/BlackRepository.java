package hu.bme.cah.api.cardsagaintshumanityapi.card.repository;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        return em.find(Black.class, id);
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
            return result;
        }
    }

    @Transactional
    public void deleteById(long id) {
        Black black = findByBlackId(id);
        em.remove(black);
    }

    public List<Black> getRandomOfBlacks(int num) {
        List<Black> rndBlacks = new ArrayList<>();
        List<Black> blacks = findAll();
        int size = blacks.size();
        List<Integer> generatedIds = getRandomBlackIds(size, num);
        for (Integer id : generatedIds) {
            rndBlacks.add(blacks.get(id));
        }
        return rndBlacks;
    }

    private List<Integer> getRandomBlackIds(int size, int num) {
        List<Integer> ids = new ArrayList<>();
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < num; i++) {
            int rndId = rnd.nextInt(size);
            while (ids.contains(rndId)) {
                rndId = rnd.nextInt(size);
            }
            ids.add(rndId);
        }
        return ids;
    }
}