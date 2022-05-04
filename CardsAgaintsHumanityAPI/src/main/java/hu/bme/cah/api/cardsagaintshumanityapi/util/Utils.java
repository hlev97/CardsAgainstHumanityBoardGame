package hu.bme.cah.api.cardsagaintshumanityapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static List<Integer> getRandomIds(int size, int num) {
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
