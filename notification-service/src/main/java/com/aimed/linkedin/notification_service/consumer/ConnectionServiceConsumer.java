package com.aimed.linkedin.notification_service.consumer;

import com.aimed.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.aimed.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.aimed.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;
    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        String message = "You have received a connection request from the user with id : %d" + sendConnectionRequestEvent.getSenderId();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        String message = "Your connection request has been accepted by the user with id : %d" + acceptConnectionRequestEvent.getSenderId();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }
}
