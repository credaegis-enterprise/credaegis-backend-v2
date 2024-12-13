package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.http.request.ClusterCreationRequest;
import com.credaegis.backend.http.request.RenameRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.ClusterNameAndIdResponse;
import com.credaegis.backend.service.ClusterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/cluster-control")
@AllArgsConstructor
public class ClusterController {

    private final ClusterService clusterService;

    @PostMapping(path = "/create")
    public ResponseEntity<CustomApiResponse<Void>> clusterCreationController(@Valid @RequestBody ClusterCreationRequest clusterCreationRequest,
                                                                             @AuthenticationPrincipal CustomUser customUser) {
        clusterService.createCluster(clusterCreationRequest, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
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
    public ResponseEntity<CustomApiResponse<Void>> renameCluster(@PathVariable String id, @Valid @RequestBody RenameRequest renameRequest,
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
                new CustomApiResponse<>(null, "Cluster admin Changed", true)
        );
    }


    @PutMapping(path = "/permissions/lock/{id}")
    public ResponseEntity<CustomApiResponse<Void>> lockClusterController(@PathVariable String id,
                                                                         @AuthenticationPrincipal CustomUser customUser) {

        clusterService.lockPermissions(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster admin Locked", true)
        );
    }


    @PutMapping(path = "/permissions/unlock/{id}")
    public ResponseEntity<CustomApiResponse<Void>> unlockClusterController(@PathVariable String id,
                                                                           @AuthenticationPrincipal CustomUser customUser) {
        clusterService.unlockPermissions(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Cluster admin Unlocked", true)
        );
    }

    @GetMapping(path = "/get-clusters")
    public ResponseEntity<CustomApiResponse<List<ClusterNameAndIdResponse>>> getClustersNameAndId(@AuthenticationPrincipal CustomUser customUser) {

        //DI at runtime
        List<ClusterNameAndIdResponse> clusters = clusterService.getAllNameAndId(customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(clusters,"Cluster List", true)
        );
    }


    @GetMapping(path = "/get-all")
    public ResponseEntity<CustomApiResponse<List<Cluster>>> getAllClusters(@AuthenticationPrincipal CustomUser customUser) {
        List<Cluster> clusters = clusterService.getAllClusters(customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(clusters,"Cluster List", true)
        );
    }


    @GetMapping(path = "/get-one/{id}")
    public ResponseEntity<CustomApiResponse<Cluster>> getOneCluster(@PathVariable String id, @AuthenticationPrincipal CustomUser customUser) {
        Cluster cluster = clusterService.getOneCluster(customUser.getOrganizationId(),id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(cluster,"details of cluster "+cluster.getName()+" fetched", true)
        );
    }

}
