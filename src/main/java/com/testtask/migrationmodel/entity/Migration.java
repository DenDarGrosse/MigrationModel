package com.testtask.migrationmodel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table
@Data
@AllArgsConstructor
public class Migration {
    @PrimaryKey
    private Long id;
    private List<String> mountPoints;
    private Long sourceId;
    private Long targetCloudId;
    private MigrationState migrationState;
}
