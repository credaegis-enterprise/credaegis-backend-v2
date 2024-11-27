package com.credaegis.backend.controller;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.service.ClusterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping(path = "/deactivate/{id}")
    public void  deactivateClusterController(@PathVariable String id, Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();
        clusterService.deactivateCluster(id,user.getOrganizationId());
    }




}
