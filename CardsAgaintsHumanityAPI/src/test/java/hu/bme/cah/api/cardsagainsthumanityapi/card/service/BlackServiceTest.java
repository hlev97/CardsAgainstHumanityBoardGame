package hu.bme.cah.api.cardsagainsthumanityapi.card.service;

import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BlackServiceTest {
    @Autowired
    BlackService blackService;

    @Test
    void save() {
        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        int sizeBefore = blackService.list().size();
        blackService.save(newCard);
        int sizeAfter = blackService.list().size();

        assertEquals( sizeBefore + 1, sizeAfter);
        List<Black> blacks = blackService.list();
        assertEquals("Test text", blacks.get(blacks.size()-1).getText());
    }

    @Test
    void testSave() {
        Black newCard1 = new Black();
        newCard1.setText("Test text");
        newCard1.setPack("Test pack");

        Black newCard2 = new Black();
        newCard2.setText("Test text");
        newCard2.setPack("Test pack");

        List<Black> newCards = List.of(newCard1, newCard2);
        int sizeBefore = blackService.list().size();
        blackService.save(newCards);
        int sizeAfter = blackService.list().size();

        assertEquals( sizeBefore + newCards.size(), sizeAfter);
    }

    @Test
    void getByBlackId() {
        int size = blackService.list().size();

        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");

        blackService.save(newCard);

        assertEquals(newCard.getText(), blackService.getByBlackId(size+1).getText());
    }

    @Test
    void update() {
        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        blackService.save(newCard);

        long id = blackService.list().size();
        Black updateCard = new Black();
        newCard.setText("Updated text");
        newCard.setPack("Updated pack");
        newCard.setBlackId(id);

        blackService.update(id, updateCard);

        assertEquals(updateCard.getText(), blackService.getByBlackId(id).getText());
    }

    @Test
    void delete() {
        Black newCard = new Black();
        newCard.setText("Test text");
        newCard.setPack("Test pack");
        blackService.save(newCard);

        long id = blackService.list().size();

        int sizeBefore = blackService.list().size();
        blackService.delete(id);
        int sizeAfter = blackService.list().size();

        assertEquals(sizeBefore-1, sizeAfter);
    }
}