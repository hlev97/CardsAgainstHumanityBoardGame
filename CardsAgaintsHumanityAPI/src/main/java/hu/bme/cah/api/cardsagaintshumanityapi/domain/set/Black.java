package hu.bme.cah.api.cardsagaintshumanityapi.domain.set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Black {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "pack")
    private int pack;

    @Column(name = "text")
    private String text;
}
