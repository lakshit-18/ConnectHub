package com.aimed.linkedin.connections_service.service;

import com.aimed.linkedin.connections_service.auth.UserContextHolder;
import com.aimed.linkedin.connections_service.entity.Person;
import com.aimed.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.aimed.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.aimed.linkedin.connections_service.repository.PersonsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {
    private final PersonsRepository personsRepository;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;

    public List<Person> getMyFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections of the user with userId : {}", userId);
        return personsRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Trying to send connection request, sender : {}, reciever : {}", senderId, receiverId);
        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Sender and receiver of the connection request cannot be same");
        }
        boolean alreadySentRequest = personsRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new RuntimeException("Connection request already exists, please wait for the receiver's response");
        }

        boolean alreadyConnected = personsRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new RuntimeException("You are already connected with the user");
        }

        log.info("Successfully sent the connection request");
        personsRepository.addConnectionRequest(senderId, receiverId);
        ;
        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);

        return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        boolean connectionRequestExists = personsRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No connection request exists between the two users");
        }
        personsRepository.acceptConnectionRequest(senderId, receiverId);

        log.info("Successfully accepted the connection request, sender : {}, receiver: {}", senderId, receiverId);
        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);

        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        boolean connectionRequestExists = personsRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No connection request exists to delete");
        }
        personsRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }

//    public List<Person> getFirstDegreeConnections(Long userId) {
//        log.info("Getting first degree connections of the user with userId : {}", userId);
//        return personsRepository.getFirstDegreeConnections(userId);
//    }
}
