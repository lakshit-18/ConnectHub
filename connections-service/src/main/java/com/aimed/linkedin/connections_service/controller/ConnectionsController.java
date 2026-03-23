package com.aimed.linkedin.connections_service.controller;

import com.aimed.linkedin.connections_service.entity.Person;
import com.aimed.linkedin.connections_service.service.ConnectionsService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {
    private final ConnectionsService connectionsService;

    @GetMapping("/first-connections")
    public ResponseEntity<List<Person>> getMyFirstConnections() {
        return ResponseEntity.ok(connectionsService.getMyFirstDegreeConnections());
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.sendConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.rejectConnectionRequest(userId));
    }

//    @GetMapping("/first-connections/{userId}")
//    public ResponseEntity<List<Person>> getFirstConnectionsOfUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections(userId));
//    }
}
