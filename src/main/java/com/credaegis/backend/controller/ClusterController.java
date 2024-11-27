package com.credaegis.backend.controller;

import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.service.ClusterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/cluster")
public class ClusterController {


    @Autowired
    ClusterService clusterService;

    @PostMapping(path = "/create")
    public void clusterCreationController(@Valid @RequestBody ClusterCreationRequest clusterCreationRequest){
        clusterService.createCluster(clusterCreationRequest);
    }


}
