package com.testtask.migrationmodel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
@AllArgsConstructor
public class TargetCloud {
    @PrimaryKey
    private Long id;
    private CloudType cloudType;
    private Long cloudCredentialsId;
    private Long targetId;
}
