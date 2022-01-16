package com.testtask.migrationmodel.repository;

import com.testtask.migrationmodel.entity.TargetCloud;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TargetCloudRepository extends CrudRepository<TargetCloud, Long>, IGetLastId {
    @Override
    @Query("SELECT MAX(id) FROM targetcloud")
    Long getLastId();
}
