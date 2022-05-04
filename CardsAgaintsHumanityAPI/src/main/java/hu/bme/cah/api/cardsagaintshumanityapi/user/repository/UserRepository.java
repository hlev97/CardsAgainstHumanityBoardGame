package hu.bme.cah.api.cardsagaintshumanityapi.user.repository;

import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}

