package hu.bme.cah.api.cardsagaintshumanityapi.domain.room;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Room {
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

    @OneToOne
    private User czar;

    @OneToMany
    private List<User> players;

    @Column(name = "private")
    private boolean privateRoom;

    @Column(name = "key")
    private String key;

}
