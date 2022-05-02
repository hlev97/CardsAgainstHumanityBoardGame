package hu.bme.cah.api.cardsagaintshumanityapi.card.repository;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public List<White> getRandomOfWhites(int num) {
        List<White> rndWhites = new ArrayList<>();
        List<White> whites = findAll();
        int size = whites.size();
        List<Integer> generatedIds = getRandomWhiteIds(size, num);
        for (Integer id : generatedIds) {
            rndWhites.add(whites.get(id));
        }
        return rndWhites;
    }

    private List<Integer> getRandomWhiteIds(int size, int num) {
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

    @Transactional
    public White update(long whiteId, White white) {
        White result = findByWhiteId(whiteId);
        if (result == null) throw new EntityNotFoundException();
        else {
            result.setText(white.getText());
            result.setPack(white.getPack());
            return result;
        }
    }

    @Transactional
    public void deleteById(long id) {
        White white = findByWhiteId(id);
        em.remove(white);
    }
}