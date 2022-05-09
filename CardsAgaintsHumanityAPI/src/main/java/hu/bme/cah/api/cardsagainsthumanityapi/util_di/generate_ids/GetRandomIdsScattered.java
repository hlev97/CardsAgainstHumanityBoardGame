package hu.bme.cah.api.cardsagainsthumanityapi.util_di.generate_ids;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class GetRandomIdsScattered implements GenerateIds {
    @Override
    public List<Integer> getRandomIds(int size, int num) {
        log.info("Generate random id scattered");
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
