package org.example.springsec.springsec.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springsec.springsec.entity.Role;
import org.example.springsec.springsec.entity.User;
import org.example.springsec.springsec.service.RoleService;
import org.example.springsec.springsec.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Configuration
@AllArgsConstructor
@Log4j2
public class Config implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;


    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Override
    public void run(String... args) throws Exception {
        startup();


//        generateKey();
    }

    private void startup() {
        if (roleService.getAllRoles().isEmpty()) {
            log.info("Creating roles");
            roleService.saveRole(new Role(null, "Admin"));
            roleService.saveRole(new Role(null, "User"));
            roleService.saveRole(new Role(null, "Employee"));
        }

        if (userService.getAllUsers().isEmpty()) {
            Set<Role> adminRole = new HashSet<>();
            adminRole.add(roleService.getRoleByName("Admin"));
            Set<Role> userRole = new HashSet<>();
            userRole.add(roleService.getRoleByName("User"));
            Set<Role> employeeRole = new HashSet<>();
            employeeRole.add(roleService.getRoleByName("Employee"));

            userService.saveUser(new User(null, "admin", "admin", "admin", adminRole));
            userService.saveUser(new User(null, "user", "user", "user", userRole));
            userService.saveUser(new User(null, "employee", "employee", "employee", employeeRole));
        }
    }

    private static void generateKey() {
        int keySizeBytes = 32;
        // Generate random bytes
        byte[] keyBytes = new byte[keySizeBytes];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        // Convert bytes to hex string
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        log.info("Key (Base64) is :");
        log.info(base64Key);
    }
}
