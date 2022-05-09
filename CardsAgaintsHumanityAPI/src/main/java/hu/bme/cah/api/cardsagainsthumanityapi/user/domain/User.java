package hu.bme.cah.api.cardsagainsthumanityapi.user.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

/**
 * Represents users
 */
@Entity
public class User {
    /**
     * Possible user roles
     */
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_CZAR = "ROLE_CZAR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * The id of a user
     */
    @Id
    private String username;
    /**
     * The password of a user
     */
    private String password;
    /**
     * The email of a user
     */
    private String email;

    /**
     * Tells if a user's account is expired
     */
    private boolean accountExpired;
    /**
     * Tells if a user's account is locked
     */
    private boolean accountLocked;
    /**
     * Tells if a user's credentials are expired
     */
    private boolean credentialsExpired;
    /**
     * Tells if a user's account is enabled
     */
    private boolean enabled;

    /**
     * The points from games
     */
    private int point;
    /**
     * Times of 1st place
     */
    private int gold;
    /**
     * Times of 2nd place
     */
    private int silver;
    /**
     * Times of 3rd place
     */
    private int bronze;

    /**
     * List of user's roles
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    /**
     * Getters and Setters
     */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User other = (User) o;
        if (username == null) {
            if (other.username != null)
                return false;
        }
        else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }
}
