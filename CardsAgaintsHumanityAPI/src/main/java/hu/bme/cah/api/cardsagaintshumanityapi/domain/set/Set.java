package hu.bme.cah.api.cardsagaintshumanityapi.domain.set;

import javax.persistence.*;
import java.util.List;

@Entity
public class Set {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "official")
    private boolean official;

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    @OneToMany
    private List<White> whiteCards;

    public List<White> getWhiteCards() {
        return whiteCards;
    }

    public void setWhiteCards(List<White> whiteCards) {
        this.whiteCards = whiteCards;
    }

    @OneToMany
    private List<Black> blackCards;

    public List<Black> getBlackCards() {
        return blackCards;
    }

    public void setBlackCards(List<Black> blackCards) {
        this.blackCards = blackCards;
    }

}
