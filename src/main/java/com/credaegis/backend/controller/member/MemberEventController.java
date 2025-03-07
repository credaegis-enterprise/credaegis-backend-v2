package com.credaegis.backend.controller.member;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.projection.EventInfoProjection;
import com.credaegis.backend.dto.projection.EventSearchProjection;
import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.http.request.EventModificationRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.member.MemberEventService;
import com.credaegis.backend.service.organization.EventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(value = Constants.ROUTE_V1_MEMBER + "/event-control")
@AllArgsConstructor
@RestController
public class MemberEventController {

    private final MemberEventService eventService;


    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @GetMapping(path = "/get-all")
    public ResponseEntity<CustomApiResponse<List<EventInfoProjection>>> getAllEvents(@AuthenticationPrincipal CustomUser customUser) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(eventService.getAllEvents(customUser.getClusterId()), "Events fetched", true)
        );
    }

    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PostMapping(path = "/create")
    public ResponseEntity<CustomApiResponse<Void>> createEvent(@RequestBody @Valid EventCreationRequest eventCreationRequest,
                                                               @AuthenticationPrincipal CustomUser customUser) {


        eventService.createEvent(eventCreationRequest, customUser.getId(),customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomApiResponse<>(null,
                "Event Successfully created", true));

    }



    @GetMapping(path="/event/name/search")
    public  ResponseEntity<CustomApiResponse<List<EventSearchProjection>>> searchByName(@RequestParam String name,
                                                                                        @AuthenticationPrincipal CustomUser customUser) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(eventService.searchByName(name, customUser.getClusterId()), "Events fetched", true)
        );
    }

    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PutMapping(path = "/activate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> activateEvent(@PathVariable String id,
                                                                 @AuthenticationPrincipal CustomUser customUser) {

        eventService.activateEvent(id, customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully activated", true)
        );
    }

    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PutMapping(path = "/deactivate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deactivateEvent(@PathVariable String id,
                                                                   @AuthenticationPrincipal CustomUser customUser) {
        eventService.deactivateEvent(id, customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully deactivated", true)
        );
    }

    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CustomApiResponse<Void>> updateEvent(@PathVariable  String id, @RequestBody @Valid
                                                                   EventModificationRequest eventModificationRequest,
                                                               @AuthenticationPrincipal CustomUser customUser) {
        eventService.updateEvent(eventModificationRequest, customUser.getClusterId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Event Successfully updated", true)
        );
    }
}
