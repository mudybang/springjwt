package com.ombagoes.springrestjwt.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role role(int id) {
        return roleRepository.findById(id).orElse(null);
    }
    public List<Role> roles() {
        return new ArrayList<>(roleRepository.findAll());
    }
}

