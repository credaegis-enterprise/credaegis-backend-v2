package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.http.request.EventModificationRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
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
    public ResponseEntity<CustomApiResponse<Void>> createEvent(@RequestBody @Valid EventCreationRequest eventCreationRequest,
                                                               @AuthenticationPrincipal CustomUser customUser) {


        eventService.createEvent(eventCreationRequest, customUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomApiResponse<>(null,
                "Event Successfully created", true));

    }

    @PutMapping(path = "/activate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> activateEvent(@PathVariable String id,
                                                                 @AuthenticationPrincipal CustomUser customUser) {

        eventService.activateEvent(id, customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully activated", true)
        );
    }

    @PutMapping(path = "/deactivate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deactivateEvent(@PathVariable String id,
                                                                   @AuthenticationPrincipal CustomUser customUser) {
        eventService.deactivateEvent(id, customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully deactivated", true)
        );
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CustomApiResponse<Void>> updateEvent(@PathVariable  String id, @RequestBody @Valid
                                                               EventModificationRequest eventModificationRequest,
                                                               @AuthenticationPrincipal CustomUser customUser) {
        eventService.updateEvent(eventModificationRequest, customUser.getUser().getOrganization().getId(),
                id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully updated", true)
        );
    }
}
