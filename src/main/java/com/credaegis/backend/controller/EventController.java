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
@RequestMapping(value = Constants.ROUTEV1 + "/event")
@AllArgsConstructor
public class EventController {


    private final EventService eventService;

    @PostMapping(path = "/create")
    public void createEvent(@RequestBody EventCreationRequest eventCreationRequest,
                            @AuthenticationPrincipal CustomUser customUser) {

    }


    @PutMapping(path = "/rename/{id}")
    public ResponseEntity<CustomApiResponse<Void>> renameEvent(@PathVariable String id,
                                                               @Valid @RequestBody RenameRequest renameRequest,
                                                               @AuthenticationPrincipal CustomUser customUser) {

        eventService.renameEvent(id, renameRequest.getNewName(), customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully Renamed", true)
        );
    }

    @PutMapping(path = "/activate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> activateEvent(@PathVariable String id,
                                                                 @AuthenticationPrincipal CustomUser customUser) {
        eventService.activateEvent(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully Activated", true)
        );
    }

    @PutMapping(path = "/deactivate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deactivateEvent(@PathVariable String id,
                                                                   @AuthenticationPrincipal CustomUser customUser) {
        eventService.deactivateEvent(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully Deactivated", true)
        );
    }

}
