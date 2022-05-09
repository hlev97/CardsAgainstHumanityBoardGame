package hu.bme.cah.api.cardsagainsthumanityapi.user.repository;

import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of CRUD operations
 */
public interface UserRepository extends JpaRepository<User, String> {}

