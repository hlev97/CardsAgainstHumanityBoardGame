package hu.bme.cah.api.cardsagainsthumanityapi.card.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * Represents a black card
 */
@Embeddable
@Data
@AllArgsConstructor
@Entity
@Slf4j
public class Black {
    /**
     * The id of a black card
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blackId;

    /**
     * The text on the black card
     */
    private String text;
    /**
     * The number of gaps in the card's text which need to be filled with white card texts
     */
    private int pick;
    /**
     * The name of the pack which contains the card
     */
    private String pack;


    /**
     * Getter and Setter functions
     */
    public Long getBlackId() {
        log.trace("Black getBlackId is accessed");
        return blackId;
    }

    public void setBlackId(Long blackId) {
        log.trace("Black setBlackId is accessed");
        this.blackId = blackId;
    }

    public String getText() {
        log.trace("Black getText is accessed");
        return text;
    }

    public void setText(String text) {
        log.trace("Black setText is accessed");
        this.text = text;
    }

    public int getPick() {
        log.trace("Black getPick is accessed");
        return pick;
    }

    public void setPick(int pick) {
        log.trace("Black setPick is accessed");
        this.pick = pick;
    }

    public String getPack() {
        log.trace("Black getPack is accessed");
        return pack;
    }

    public void setPack(String pack) {
        log.trace("Black setPack is accessed");
        this.pack = pack;
    }

    /**
     * Constructor
     */
    public Black() {
        log.trace("Black constructor is accessed");
    }
}
