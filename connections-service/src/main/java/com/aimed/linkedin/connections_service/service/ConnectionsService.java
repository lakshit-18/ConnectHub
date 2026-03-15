package com.aimed.linkedin.connections_service.service;

import com.aimed.linkedin.connections_service.entity.Person;
import com.aimed.linkedin.connections_service.repository.PersonsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {
    private final PersonsRepository personsRepository;

    public List<Person> getFirstDegreeConnections(Long userId) {
        log.info("Getting first degree connections of the user with userId : {}", userId);
        return personsRepository.getFirstDegreeConnections(userId);
    }
}
