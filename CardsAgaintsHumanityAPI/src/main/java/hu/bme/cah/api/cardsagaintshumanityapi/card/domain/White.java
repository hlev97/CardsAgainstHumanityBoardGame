package hu.bme.cah.api.cardsagaintshumanityapi.card.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

/**
 * {
 *     "text":"\"Tweeting.\"",
 *     "pack":"Base"
 *   }
 */
@Embeddable
@Data
@AllArgsConstructor
@Entity
public class White {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long whiteId;

    private String text;
    private String pack;

    public Long getWhiteId() {
        return whiteId;
    }

    public void setWhiteId(Long whiteId) {
        this.whiteId = whiteId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public White() {}
}
