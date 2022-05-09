package hu.bme.cah.api.cardsagainsthumanityapi.card.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * Represents a white card
 */
@Embeddable
@Data
@AllArgsConstructor
@Entity
@Slf4j
public class White {
    /**
     * The id of the card
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long whiteId;

    /**
     * The text on the white card
     */
    private String text;
    /**
     * The name of the pack which contains the card
     */
    private String pack;

    /**
     * Getter and Setter functions
     */
    public Long getWhiteId() {
        log.trace("White getWhiteId is accessed");
        return whiteId;
    }

    public void setWhiteId(Long whiteId) {
        log.trace("White setWhiteId is accessed");
        this.whiteId = whiteId;
    }

    public String getText() {
        log.trace("White getText is accessed");
        return text;
    }

    public void setText(String text) {
        log.trace("White setText is accessed");
        this.text = text;
    }

    public String getPack() {
        log.trace("White getPack is accessed");
        return pack;
    }

    public void setPack(String pack) {
        log.trace("White setPack is accessed");
        this.pack = pack;
    }

    /**
     * Constructor
     */
    public White() {}
}
