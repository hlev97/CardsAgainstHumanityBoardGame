package hu.bme.cah.api.cardsagaintshumanityapi.card.service;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.repository.BlackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlackService {
    @Autowired
    private BlackRepository blackRepository;

    public List<Black> list() {
        return blackRepository.findAll();
    }

    public Black save(Black black) {
        return blackRepository.save(black);
    }

    public void save(List<Black> blacks) {
        blackRepository.saveAll(blacks);
    }

    public Black getByBlackId(long blackId) {
        return blackRepository.findByBlackId(blackId);
    }

    public Black update(long blackId, Black black) {
        return blackRepository.update(blackId, black);
    }

    public void delete(long blackId) {
        blackRepository.deleteById(blackId);
    }

    public List<Black> getRandomOfBlacks(int num) {
        return blackRepository.getRandomOfBlacks(num);
    }
}
