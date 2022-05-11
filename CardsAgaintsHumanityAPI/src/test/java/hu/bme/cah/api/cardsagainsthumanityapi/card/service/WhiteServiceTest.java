package hu.bme.cah.api.cardsagainsthumanityapi.card.service;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class WhiteServiceTest {
    @Autowired
    WhiteService whiteService;

    @Test
    void contextLoads() {
        assertThat(whiteService).isNotNull();
    }

    @Test
    void save() {
        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        int sizeBefore = whiteService.list().size();
        whiteService.save(newCard);
        int sizeAfter = whiteService.list().size();

        assertEquals( sizeBefore + 1, sizeAfter);
        List<White> whites = whiteService.list();
        assertEquals("Test text", whites.get(whites.size()-1).getText());
    }

    @Test
    void testSave() {
        White newCard1 = new White();
        newCard1.setText("Test text");
        newCard1.setPack("Test pack");

        White newCard2 = new White();
        newCard2.setText("Test text");
        newCard2.setPack("Test pack");

        List<White> newCards = List.of(newCard1, newCard2);
        int sizeBefore = whiteService.list().size();
        whiteService.save(newCards);
        int sizeAfter = whiteService.list().size();

        assertEquals( sizeBefore + newCards.size(), sizeAfter);
    }

    @Test
    void getByWhiteId() {
        int size = whiteService.list().size();

        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        whiteService.save(newCard);

        assertEquals(newCard.getText(), whiteService.getByWhiteId(size+1).getText());
    }

    @Test
    void update() {
        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        whiteService.save(newCard);

        long id = whiteService.list().size();
        White updateCard = new White();
        newCard.setText("Updated text");
        newCard.setPack("Updated pack");
        newCard.setWhiteId(id);

        whiteService.update(id, updateCard);

        assertEquals(updateCard.getText(), whiteService.getByWhiteId(id).getText());
    }

    @Test
    void delete() {
        White newCard = new White();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        whiteService.save(newCard);

        long id = whiteService.list().size();

        int sizeBefore = whiteService.list().size();
        whiteService.delete(id);
        int sizeAfter = whiteService.list().size();

        assertEquals(sizeBefore-1, sizeAfter);
    }
}