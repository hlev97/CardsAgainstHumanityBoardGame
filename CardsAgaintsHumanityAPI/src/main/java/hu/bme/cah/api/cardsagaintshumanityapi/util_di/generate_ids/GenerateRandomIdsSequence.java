package hu.bme.cah.api.cardsagaintshumanityapi.util_di.generate_ids;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateRandomIdsSequence implements GenerateIds {
    @Override
    public List<Integer> getRandomIds(int size, int num) {
        Random rnd = new Random(System.currentTimeMillis());
        int statingId = rnd.nextInt(size-num);
        List<Integer> generatedIds = new ArrayList<>();
        for (int i = statingId; i < statingId + num; i++) {
            generatedIds.add(i);
        }
        return generatedIds;
    }
}
