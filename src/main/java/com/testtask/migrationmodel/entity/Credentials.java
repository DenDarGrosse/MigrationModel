package com.testtask.migrationmodel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table
@Data
@AllArgsConstructor
public class Credentials {

    @PrimaryKey
    @NonNull
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String domain;
}
