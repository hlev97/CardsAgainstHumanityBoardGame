package hu.bme.cah.api.cardsagainsthumanityapi.log.repository;

import hu.bme.cah.api.cardsagainsthumanityapi.log.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> { }
