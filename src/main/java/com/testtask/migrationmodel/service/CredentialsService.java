package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Credentials;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CredentialsService {
    private final CredentialsRepository credentialsRepository;

    public Credentials validate(Long credentialId) {
        var credentialsData = credentialsRepository.findById(credentialId);
        if (credentialsData.isEmpty()) {
            return null;
        }

        return credentialsData.get();
    }
}