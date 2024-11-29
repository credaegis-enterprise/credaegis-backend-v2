package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.request.EventCreationRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/event")
public class EventController {


    @PostMapping(path = "/create")
    public void createEvent(@RequestBody EventCreationRequest eventCreationRequest,
                            @AuthenticationPrincipal CustomUser customUser) {

    }
}
