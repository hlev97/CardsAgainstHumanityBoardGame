package hu.bme.cah.api.cardsagainsthumanityapi.log.service;

import hu.bme.cah.api.cardsagainsthumanityapi.log.domain.Log;
import hu.bme.cah.api.cardsagainsthumanityapi.log.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogRepository repository;

    public Log save(Log log) {
        return repository.save(log);
    }

    public List<Log> getLogs() {
        return repository.findAll();
    }
}
