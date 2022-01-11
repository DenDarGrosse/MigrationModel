package com.testtask.migrationmodel.repository;

import com.testtask.migrationmodel.entity.Migration;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MigrationRepository extends CrudRepository<Migration, Long> {
    @Query("SELECT MAX(id) FROM migration")
    Long getLastId();
}
