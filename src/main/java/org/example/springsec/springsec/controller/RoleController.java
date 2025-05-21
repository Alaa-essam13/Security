package org.example.springsec.springsec.controller;

import lombok.AllArgsConstructor;
import org.example.springsec.springsec.entity.Role;
import org.example.springsec.springsec.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<List<Role>> get() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(Long id){
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Role> getByName(String name){
        return ResponseEntity.ok(roleService.getRoleByName(name));
    }

    @PostMapping("/save")
    public ResponseEntity<Role> postRole(@RequestBody Role role) {
        roleService.saveRole(role);
        return ResponseEntity.ok(role);
    }
}
