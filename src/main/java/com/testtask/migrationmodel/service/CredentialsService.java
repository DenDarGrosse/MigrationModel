package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Credentials;
import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final IdUtil idUtil;

    public Credentials validate(Long credentialId) {
        var credentialsData = credentialsRepository.findById(credentialId);

        if (credentialsData.isEmpty()) {
            throw new NoSuchElementException("Can not find Credentials with id " + credentialId);
        }

        return credentialsData.get();
    }

    public Credentials add(Credentials credentials){
        var lastId = idUtil.getNextId(credentialsRepository);

        var _credentials = new Credentials(
                lastId,
                credentials.getUsername(),
                credentials.getPassword(),
                credentials.getDomain());
        credentialsRepository.save(_credentials);

        return _credentials;
    }

    public Credentials modify(Long id, Credentials credentials){
        var _credentials = validate(id);
        _credentials.setPassword(credentials.getPassword());
        _credentials.setDomain(credentials.getDomain());
        _credentials.setUsername(credentials.getUsername());
        credentialsRepository.save(_credentials);

        return _credentials;
    }
}
