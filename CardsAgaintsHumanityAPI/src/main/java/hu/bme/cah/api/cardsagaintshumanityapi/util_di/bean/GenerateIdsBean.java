package hu.bme.cah.api.cardsagaintshumanityapi.util_di.bean;

import hu.bme.cah.api.cardsagaintshumanityapi.util_di.generate_ids.GenerateIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerateIdsBean {
    private GenerateIds generationMethod;

    @Autowired
    public GenerateIdsBean(GenerateIds generationMethod) {
        super();
        this.generationMethod = generationMethod;
    }

    @Autowired
    public void setGenerationMethod(GenerateIds generationMethod) {
        this.generationMethod = generationMethod;
    }

    public List<Integer> randomIds(int size, int num) {
        return generationMethod.getRandomIds(size, num);
    }
}
