package com.credaegis.backend.controller.member;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;

import com.credaegis.backend.http.request.MemberCreationRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.MemberInfoResponse;
import com.credaegis.backend.service.member.MemberClusterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = Constants.ROUTE_V1_MEMBER + "/member-control")
public class MemberClusterController {

    private final MemberClusterService memberService;


    @GetMapping(path = "/get-all")
    public ResponseEntity<CustomApiResponse<MemberInfoResponse>> getAllMembers(@AuthenticationPrincipal CustomUser customUser) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(memberService.getMembers(customUser.getClusterId(),customUser.getId()), "Members fetched successfully", true)
        );
    }

    @PostMapping(path = "/create")
    public ResponseEntity<CustomApiResponse<Void>> createMember(@RequestBody @Valid MemberCreationRequest memberCreationRequest,
                                                                @AuthenticationPrincipal CustomUser customUser) {

        memberService.createMember(memberCreationRequest,customUser.getClusterId(), customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomApiResponse<>(null, "Member created Successfully", true)
        );

    }

    @PutMapping(path = "/deactivate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deactivateMember(@PathVariable String id,
                                                                    @AuthenticationPrincipal CustomUser customUser) {

        memberService.deactivateMember(id, customUser.getId(), customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member deactivated successfully", true)
        );
    }

    @PutMapping(path = "/activate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> activateMember(@PathVariable String id,
                                                                  @AuthenticationPrincipal CustomUser customUser) {
        memberService.activateMember(id, customUser.getId(), customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member activated successfully", true)
        );

    }

    @PutMapping(path = "/delete/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteMember(@PathVariable String id,
                                                                @AuthenticationPrincipal CustomUser customUser) {
        memberService.deleteMember(id, customUser.getId(), customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Member deleted successfully", true)
        );
    }




}
