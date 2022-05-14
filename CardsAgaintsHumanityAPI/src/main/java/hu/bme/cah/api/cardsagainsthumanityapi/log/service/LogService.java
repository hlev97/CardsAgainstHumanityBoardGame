package hu.bme.cah.api.cardsagainsthumanityapi.log.service;

import hu.bme.cah.api.cardsagainsthumanityapi.log.domain.Log;
import hu.bme.cah.api.cardsagainsthumanityapi.log.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ensures that the logs can be saved to the database through {@link LogRepository}
 */
@Service
public class LogService {
    /**
     * LogRepository for operations
     */
    @Autowired
    private LogRepository repository;

    /**
     * Saves a new log
     * @param log log
     * @return saved log
     */
    public Log save(Log log) {
        return repository.save(log);
    }

    /**
     * Return all saved logs
     * @return logs
     */
    public List<Log> getLogs() {
        return repository.findAll();
    }
}
