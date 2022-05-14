package hu.bme.cah.api.cardsagainsthumanityapi.util_di.generate_ids;

import java.util.List;

/**
 * Id generation interface
 */
public interface GenerateIds {
    List<Integer> getRandomIds(int size, int num);
}
