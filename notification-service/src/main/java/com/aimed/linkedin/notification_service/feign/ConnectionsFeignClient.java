package com.aimed.linkedin.notification_service.feign;

import com.aimed.linkedin.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsFeignClient {
    @GetMapping("/core/first-connections")
    List<PersonDto> getMyFirstConnections(@RequestHeader("X-User-Id") Long userId);
}
