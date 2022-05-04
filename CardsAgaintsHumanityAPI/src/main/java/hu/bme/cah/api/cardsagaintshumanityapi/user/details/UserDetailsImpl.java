package hu.bme.cah.api.cardsagaintshumanityapi.user.details;

import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public double getPoint() {
        return user.getPoint();
    }

    public void setPoint(double point) {
        user.setPoint(point);
    }

    public int getGold() {
        return user.getGold();
    }

    public void setGold(int gold) {
        user.setGold(gold);
    }

    public int getSilver() {
        return user.getSilver();
    }

    public void setSilver(int silver) {
        user.setSilver(silver);
    }

    public int getBronze() {
        return user.getBronze();
    }

    public void setBronze(int bronze) {
        user.setBronze(bronze);
    }
}
