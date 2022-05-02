package hu.bme.cah.api.cardsagaintshumanityapi.card.service;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.repository.WhiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhiteService {
    @Autowired
    private WhiteRepository whiteRepository;

    public List<White> list() {
        return whiteRepository.findAll();
    }

    public White save(White white) {
        return whiteRepository.save(white);
    }

    public void save(List<White> whites) {
        whiteRepository.saveAll(whites);
    }

    public White getByWhiteId(long whiteId) {
        return whiteRepository.findByWhiteId(whiteId);
    }

    public White update(long whiteId, White white) {
        return whiteRepository.update(whiteId, white);
    }

    public void delete(long whiteId) {
        whiteRepository.deleteById(whiteId);
    }

    public List<White> getRandomOfWhites(int num) {
        return whiteRepository.getRandomOfWhites(num);
    }
}
