package com.credaegis.backend.controller;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.dto.request.RenameRequest;
import com.credaegis.backend.service.ClusterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/cluster")
@AllArgsConstructor
public class ClusterController {

    private final ClusterService clusterService;

    @PostMapping(path = "/create")
    public void clusterCreationController(@Valid @RequestBody ClusterCreationRequest clusterCreationRequest,
                                          Authentication authentication){

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        clusterService.createCluster(clusterCreationRequest,customUser.getOrganizationId());
    }

    @PutMapping(path = "/activate/{id}")
    public void  activateClusterController(@PathVariable  String id, Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();
        clusterService.activateCluster(id,user.getOrganizationId());
    }

    @PutMapping(path = "/deactivate/{id}")
    public void  deactivateClusterController(@PathVariable  String id, Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();
        clusterService.deactivateCluster(id,user.getOrganizationId());
    }

    @PutMapping(path = "/rename/{id}")
    public void renameCluster(@PathVariable String id, @RequestBody RenameRequest renameRequest, Authentication authentication){
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        clusterService.renameCluster(id,customUser.getOrganizationId(),renameRequest.getNewName());

    }

    @PutMapping(path = "/change-admin/{clusterId}/{newAdminId}")
    public void changeAdmin(@PathVariable String clusterId, @PathVariable String newAdminId,
                            Authentication authentication){

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        clusterService.changeAdmin(clusterId,newAdminId,customUser.getOrganizationId());
    }


}
