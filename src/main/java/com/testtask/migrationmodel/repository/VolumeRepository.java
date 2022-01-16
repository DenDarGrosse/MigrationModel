package com.testtask.migrationmodel.repository;

import com.testtask.migrationmodel.entity.Volume;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VolumeRepository extends CrudRepository<Volume, Long>, IGetLastId {
    @Override
    @Query("SELECT MAX(id) FROM Volume")
    Long getLastId();
}
