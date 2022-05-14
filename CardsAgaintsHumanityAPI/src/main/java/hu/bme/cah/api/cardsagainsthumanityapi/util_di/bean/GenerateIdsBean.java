package hu.bme.cah.api.cardsagainsthumanityapi.util_di.bean;

import hu.bme.cah.api.cardsagainsthumanityapi.util_di.generate_ids.GenerateIds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component id generation method for Dependency Injection
 */
@Component
@Slf4j
public class GenerateIdsBean {
    /**
     * Generation method
     */
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
        log.trace("GenerateIdsBean.randomIds(size,num) method is accessed");
        return generationMethod.getRandomIds(size, num);
    }
}
