package com.testtask.migrationmodel.util;

import com.testtask.migrationmodel.repository.IGetLastId;
import org.springframework.stereotype.Component;

@Component
public class IdUtil {
    public Long getNextId(IGetLastId repository){
        var lastId = repository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }

        return lastId;
    }
}
