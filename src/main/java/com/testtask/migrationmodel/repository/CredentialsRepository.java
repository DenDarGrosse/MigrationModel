package com.testtask.migrationmodel.repository;

import com.testtask.migrationmodel.entity.Credentials;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CredentialsRepository extends CrudRepository<Credentials, Long>, IGetLastId {
    @Override
    @Query("SELECT MAX(id) FROM credentials")
    Long getLastId();
}
