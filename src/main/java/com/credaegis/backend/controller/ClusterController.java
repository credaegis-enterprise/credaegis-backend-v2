package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.dto.request.RenameRequest;
import com.credaegis.backend.service.ClusterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/cluster")
@AllArgsConstructor
public class ClusterController {

    private final ClusterService clusterService;

    @PostMapping(path = "/create")
    public void clusterCreationController(@Valid @RequestBody ClusterCreationRequest clusterCreationRequest,
                                          @AuthenticationPrincipal CustomUser customUser){
        clusterService.createCluster(clusterCreationRequest,customUser.getOrganizationId());
    }

    @PutMapping(path = "/activate/{id}")
    public void  activateClusterController(@PathVariable  String id,
                                           @AuthenticationPrincipal CustomUser customUser){
        clusterService.activateCluster(id,customUser.getOrganizationId());
    }

    @PutMapping(path = "/deactivate/{id}")
    public void  deactivateClusterController(@PathVariable  String id, @AuthenticationPrincipal CustomUser customUser){
        clusterService.deactivateCluster(id,customUser.getOrganizationId());
    }

    @PutMapping(path = "/rename/{id}")
    public void renameCluster(@PathVariable String id, @RequestBody RenameRequest renameRequest,
                              @AuthenticationPrincipal CustomUser customUser){
        clusterService.renameCluster(id,customUser.getOrganizationId(),renameRequest.getNewName());

    }

    @PutMapping(path = "/change-admin/{clusterId}/{newAdminId}")
    public void changeAdmin(@PathVariable String clusterId, @PathVariable String newAdminId,
                            @AuthenticationPrincipal CustomUser customUser){

        clusterService.changeAdmin(clusterId,newAdminId,customUser.getOrganizationId());
    }


}
