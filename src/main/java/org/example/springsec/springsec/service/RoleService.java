package org.example.springsec.springsec.service;

import lombok.AllArgsConstructor;
import org.example.springsec.springsec.entity.Role;
import org.example.springsec.springsec.repo.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;
    public void saveRole(Role role){
        roleRepo.save(role);
    }
    public Role getRoleByName(String name){
        return roleRepo.findByName(name);
    }
    public Role getRoleById(Long id){
        return roleRepo.findById(id).orElse(null);
    }
    public List<Role> getAllRoles(){
        return roleRepo.findAll();
    }

}
