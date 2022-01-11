package com.testtask.migrationmodel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
@AllArgsConstructor
public class Volume {
    @PrimaryKey
    private Long id;
    private String mountPoint;
    private int size;
}
