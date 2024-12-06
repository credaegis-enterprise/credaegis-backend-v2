package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.http.request.RenameRequest;
import com.credaegis.backend.http.response.custom.api.CustomApiResponse;
import com.credaegis.backend.service.EventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/event-control")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(path = "/create")
    public ResponseEntity<CustomApiResponse<Void>> createEvent(@RequestBody @Valid  EventCreationRequest eventCreationRequest,
                                                               @AuthenticationPrincipal CustomUser customUser) {


        eventService.createEvent(eventCreationRequest,customUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomApiResponse<>(null,
                "Event Successfully created",true));

    }
}
