package com.credaegis.backend.controller;


import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.request.MemberCreationRequest;
import com.credaegis.backend.dto.request.RenameRequest;
import com.credaegis.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = Constants.ROUTEV1+"/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(path = "/create")
    public void createMember(@RequestBody MemberCreationRequest memberCreationRequest,
                             @AuthenticationPrincipal CustomUser customUser) {

        memberService.createMember(memberCreationRequest,customUser.getOrganizationId());

    }

    @PutMapping(path = "/deactivate/{id}")
    public void deactivateMember(@PathVariable String id,
                                 @AuthenticationPrincipal CustomUser customUser) {

        memberService.deactivateMember(id,customUser.getUserId(),customUser.getOrganizationId());
    }

    @PutMapping(path = "/activate/{id}")
    public void activateMember(@PathVariable String id,
                               @AuthenticationPrincipal CustomUser customUser) {
        memberService.activateMember(id,customUser.getUserId(),customUser.getOrganizationId());

    }

    @PutMapping(path = "/delete/{id}")
    public void deleteMember(@PathVariable String id,
                             @AuthenticationPrincipal CustomUser customUser) {
        memberService.deleteMember(id,customUser.getUserId(),customUser.getOrganizationId());
    }

    @PutMapping(path="/rename/{id}")
    public void renameMember(@PathVariable String id, @Valid @RequestBody RenameRequest renameRequest,
                             @AuthenticationPrincipal CustomUser customUser) {

        memberService.renameUser(id,renameRequest.getNewName(),customUser.getOrganizationId());
    }



}
