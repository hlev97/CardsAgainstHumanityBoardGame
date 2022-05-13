package hu.bme.cah.api.cardsagainsthumanityapi.card.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BlackRepositoryTest {

    @Autowired
    BlackRepository blackRepository;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper mapperBlack = new ObjectMapper();
        TypeReference<List<Black>> typeReferenceBlack = new TypeReference<>(){};
        InputStream inputStreamBlack = TypeReference.class.getResourceAsStream("/database/black.json");

        List<Black> blacks = mapperBlack.readValue(inputStreamBlack,typeReferenceBlack);
        blackRepository.saveAll(blacks);
    }

    @Test
    void contextLoads() {
        assertThat(blackRepository).isNotNull();
    }

    @Test
    void save() {
        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        int sizeBefore = blackRepository.findAll().size();
        blackRepository.save(newCard);
        int sizeAfter = blackRepository.findAll().size();

        assertEquals( sizeBefore + 1, sizeAfter);
        List<Black> blacks = blackRepository.findAll();
        assertEquals("Test text", blacks.get(blacks.size()-1).getText());
    }

    @Test
    void saveAll() {
        Black newCard1 = new Black();
        newCard1.setText("Test text");
        newCard1.setPack("Test pack");

        Black newCard2 = new Black();
        newCard2.setText("Test text");
        newCard2.setPack("Test pack");

        List<Black> newCards = List.of(newCard1, newCard2);
        int sizeBefore = blackRepository.findAll().size();
        blackRepository.saveAll(newCards);
        int sizeAfter = blackRepository.findAll().size();

        assertEquals( sizeBefore + newCards.size(), sizeAfter);
    }

    @Test
    void findByBlackId() {
        int size = blackRepository.findAll().size();

        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        blackRepository.save(newCard);

        assertEquals(newCard.getText(), blackRepository.findByBlackId(size+1).getText());
    }

    @Test
    void update() {
        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        blackRepository.save(newCard);

        long id = blackRepository.findAll().size();
        Black updateCard = new Black();
        newCard.setText("Updated text");
        newCard.setPack("Updated pack");
        newCard.setBlackId(id);

        blackRepository.update(id, updateCard);

        assertEquals(updateCard.getText(), blackRepository.findByBlackId(id).getText());
    }

    @Test
    void deleteById() {
        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        blackRepository.save(newCard);

        long id = blackRepository.findAll().size();

        int sizeBefore = blackRepository.findAll().size();
        blackRepository.deleteById(id);
        int sizeAfter = blackRepository.findAll().size();

        assertEquals(sizeBefore-1, sizeAfter);
    }
}