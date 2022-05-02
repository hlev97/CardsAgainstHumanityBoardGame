package hu.bme.cah.api.cardsagaintshumanityapi.card.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * {
 *     "text":"Your security clearance has been suspended beause of your shameful past involving _.",
 *     "pick":1,
 *     "pack":"Base"
 * }
 */
@Data
@AllArgsConstructor
@Entity
public class Black {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blackId;

    private String text;
    private int pick;
    private String pack;

    public Long getBlackId() {
        return blackId;
    }

    public void setBlackId(Long blackId) {
        this.blackId = blackId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPick() {
        return pick;
    }

    public void setPick(int pick) {
        this.pick = pick;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public Black() {}
}
