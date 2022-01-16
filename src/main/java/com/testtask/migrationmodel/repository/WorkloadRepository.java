package com.testtask.migrationmodel.repository;

import com.testtask.migrationmodel.entity.Workload;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WorkloadRepository extends CrudRepository<Workload, Long>, IGetLastId {
    @Override
    @Query("SELECT MAX(id) FROM workload")
    Long getLastId();
}
