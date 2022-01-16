package com.testtask.migrationmodel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.List;

@Table
@Data
@AllArgsConstructor
public class Workload {
    @PrimaryKey
    private Long id;
    @NonNull
    private final String ip;
    private Long credentialsId;
    private List<Long> volumeIds;
}
