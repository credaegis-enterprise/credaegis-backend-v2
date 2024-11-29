package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.dto.request.RenameRequest;
import com.credaegis.backend.dto.response.custom.api.CustomApiResponse;
import com.credaegis.backend.service.ClusterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/cluster")
@AllArgsConstructor
public class ClusterController {

    private final ClusterService clusterService;

    @PostMapping(path = "/create")
    public ResponseEntity<CustomApiResponse<Void>> clusterCreationController(@Valid @RequestBody ClusterCreationRequest clusterCreationRequest,
                                                                             @AuthenticationPrincipal CustomUser customUser) {
        clusterService.createCluster(clusterCreationRequest, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster Successfully created", true)
        );
    }

    @PutMapping(path = "/activate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> activateClusterController(@PathVariable String id,
                                                                             @AuthenticationPrincipal CustomUser customUser) {
        clusterService.activateCluster(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster Activated", true)
        );
    }

    @PutMapping(path = "/deactivate/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deactivateClusterController(@PathVariable String id, @AuthenticationPrincipal
    CustomUser customUser) {
        clusterService.deactivateCluster(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster Deactivated", true)
        );
    }

    @PutMapping(path = "/rename/{id}")
    public ResponseEntity<CustomApiResponse<Void>> renameCluster(@PathVariable String id, @RequestBody RenameRequest renameRequest,
                              @AuthenticationPrincipal CustomUser customUser) {
        clusterService.renameCluster(id, customUser.getOrganizationId(), renameRequest.getNewName());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster Renamed", true)
        );

    }

    @PutMapping(path = "/change-admin/{clusterId}/{newAdminId}")
    public ResponseEntity<CustomApiResponse<Void>> changeAdmin(@PathVariable String clusterId, @PathVariable String newAdminId,
                            @AuthenticationPrincipal CustomUser customUser) {

        clusterService.changeAdmin(clusterId, newAdminId, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster Admin Changed", true)
        );
    }


}
