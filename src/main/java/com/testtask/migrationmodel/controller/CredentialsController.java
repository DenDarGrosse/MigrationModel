package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Credentials;
import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.service.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credentials")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CredentialsController {
    private final CredentialsService credentialsService;
    private final CredentialsRepository credentialsRepository;

    @PostMapping
    public ResponseEntity<Credentials> add(@RequestBody Credentials credentials) {
        var _credentials = credentialsService.add(credentials);
        return ResponseEntity.ok(_credentials);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Credentials> modify(@PathVariable("id") Long id, @RequestBody Credentials credentials) {
        var _credentials = credentialsService.modify(id, credentials);
        return ResponseEntity.ok(_credentials);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") Long id) {
        credentialsRepository.deleteById(id);
    }
}
