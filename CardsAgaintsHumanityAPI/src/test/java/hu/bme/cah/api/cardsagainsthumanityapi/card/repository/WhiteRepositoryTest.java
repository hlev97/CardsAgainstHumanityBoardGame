package hu.bme.cah.api.cardsagainsthumanityapi.card.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
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
class WhiteRepositoryTest {
    @Autowired
    WhiteRepository whiteRepository;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper mapperWhite = new ObjectMapper();
        TypeReference<List<White>> typeReferenceWhite = new TypeReference<>(){};
        InputStream inputStreamWhite = TypeReference.class.getResourceAsStream("/database/white.json");

        List<White> whites = mapperWhite.readValue(inputStreamWhite,typeReferenceWhite);
        whiteRepository.saveAll(whites);
    }

    @Test
    void contextLoads() {
        assertThat(whiteRepository).isNotNull();
    }

    @Test
    void save() {
        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        int sizeBefore = whiteRepository.findAll().size();
        whiteRepository.save(newCard);
        int sizeAfter = whiteRepository.findAll().size();

        assertEquals( sizeBefore + 1, sizeAfter);
        List<White> whites = whiteRepository.findAll();
        assertEquals("Test text", whites.get(whites.size()-1).getText());
    }

    @Test
    void saveAll() {
        White newCard1 = new White();
        newCard1.setText("Test text");
        newCard1.setPack("Test pack");

        White newCard2 = new White();
        newCard2.setText("Test text");
        newCard2.setPack("Test pack");

        List<White> newCards = List.of(newCard1, newCard2);
        int sizeBefore = whiteRepository.findAll().size();
        whiteRepository.saveAll(newCards);
        int sizeAfter = whiteRepository.findAll().size();

        assertEquals( sizeBefore + newCards.size(), sizeAfter);
    }

    @Test
    void findByWhiteId() {
        int size = whiteRepository.findAll().size();

        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        whiteRepository.save(newCard);

        assertEquals(newCard.getText(), whiteRepository.findByWhiteId(size+1).getText());
    }

    @Test
    void update() {
        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        whiteRepository.save(newCard);

        long id = whiteRepository.findAll().size();
        White updateCard = new White();
        newCard.setText("Updated text");
        newCard.setPack("Updated pack");
        newCard.setWhiteId(id);

        whiteRepository.update(id, updateCard);

        assertEquals(updateCard.getText(), whiteRepository.findByWhiteId(id).getText());
    }

    @Test
    void deleteById() {
        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        whiteRepository.save(newCard);

        long id = whiteRepository.findAll().size();

        int sizeBefore = whiteRepository.findAll().size();
        whiteRepository.deleteById(id);
        int sizeAfter = whiteRepository.findAll().size();

        assertEquals(sizeBefore-1, sizeAfter);
    }
}