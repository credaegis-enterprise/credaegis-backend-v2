package com.credaegis.backend.repository;

import com.credaegis.backend.entity.BatchInfo;
import jnr.ffi.annotations.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchInfoRepository extends JpaRepository<BatchInfo, Integer> {
}
