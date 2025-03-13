package com.credaegis.backend.repository;


import com.credaegis.backend.entity.BatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
BatchInfoRepository extends JpaRepository<BatchInfo, Integer> {

   Optional<BatchInfo> findOneByMerkleRoot(String merkleRoot);
   Optional<BatchInfo> findOneById(int i);
}