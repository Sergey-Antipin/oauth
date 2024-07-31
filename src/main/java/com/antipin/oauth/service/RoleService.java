package com.antipin.oauth.service;

import com.antipin.oauth.exception.EntityNotFoundException;
import com.antipin.oauth.model.Role;
import com.antipin.oauth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public Role getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public Role getByRole(String role) {
        return repository.findByRole(role).orElseThrow(() -> new EntityNotFoundException(role));
    }
}
