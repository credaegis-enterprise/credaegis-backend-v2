package com.credaegis.backend.controller;


import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.http.request.MemberCreationRequest;
import com.credaegis.backend.http.request.RenameRequest;
import com.credaegis.backend.http.response.custom.api.CustomApiResponse;
import com.credaegis.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/member-control")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(path = "/create")
    public ResponseEntity<CustomApiResponse<Void>> createMember(@RequestBody @Valid MemberCreationRequest memberCreationRequest,
                                                                @AuthenticationPrincipal CustomUser customUser) {

        memberService.createMember(memberCreationRequest, customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomApiResponse<>(null, "Member created Successfully", true)
        );

    }

    @PutMapping(path = "/deactivate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deactivateMember(@PathVariable String id,
                                                                    @AuthenticationPrincipal CustomUser customUser) {

        memberService.deactivateMember(id, customUser.getUser().getId(), customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member deactivated successfully", true)
        );
    }

    @PutMapping(path = "/activate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> activateMember(@PathVariable String id,
                                                                  @AuthenticationPrincipal CustomUser customUser) {
        memberService.activateMember(id, customUser.getUser().getId(), customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member activated successfully", true)
        );

    }

    @PutMapping(path = "/delete/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteMember(@PathVariable String id,
                                                                @AuthenticationPrincipal CustomUser customUser) {
        memberService.deleteMember(id, customUser.getUser().getId(), customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member deleted successfully", true)
        );
    }

    @PutMapping(path = "/rename/{id}")
    public ResponseEntity<CustomApiResponse<Void>> renameMember(@PathVariable String id, @Valid @RequestBody RenameRequest renameRequest,
                                                                @AuthenticationPrincipal CustomUser customUser) {

        memberService.renameUser(id, renameRequest.getNewName(), customUser.getUser().getOrganization().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member renamed successfully", true)
        );
    }


}
