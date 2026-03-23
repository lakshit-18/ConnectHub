package com.aimed.linkedin.posts_service.feign;

import com.aimed.linkedin.posts_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsFeignClient {
    @GetMapping("/core/first-connections")
    List<PersonDto> getMyFirstConnections();
}
