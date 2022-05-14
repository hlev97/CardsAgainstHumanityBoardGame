package hu.bme.cah.api.cardsagainsthumanityapi.util_di.generate_ids;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random integer sequence between 0 and a given number
 */
@Slf4j
public class GenerateRandomIdsSequence implements GenerateIds {
    @Override
    public List<Integer> getRandomIds(int size, int num) {
        log.trace("GenerateRandomIdsSequence.getRandomIds(size,num) method is accessed");
        log.info("Generate random id sequence");
        Random rnd = new Random(System.currentTimeMillis());
        int statingId = rnd.nextInt(size-num);
        List<Integer> generatedIds = new ArrayList<>();
        for (int i = statingId; i < statingId + num; i++) {
            generatedIds.add(i);
        }
        return generatedIds;
    }
}
