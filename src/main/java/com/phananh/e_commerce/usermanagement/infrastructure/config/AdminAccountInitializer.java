package com.phananh.e_commerce.usermanagement.infrastructure.config;

import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.UserCredentials;
import com.phananh.e_commerce.usermanagement.domain.model.UserInfo;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdminAccountInitializer implements ApplicationRunner {

    private final SpringDataUserRepository springDataUserRepository;
    private final SpringDataRoleRepository springDataRoleRepository;
    private final AdminSeedProperties adminSeedProperties;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        seedRoles();

        if (springDataUserRepository.existsByCredentialsUsername(adminSeedProperties.username())) {
            return;
        }

        Role adminRole = springDataRoleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("Cannot initialize admin role"));

        User adminUser = new User();
        setField(adminUser, "credentials", UserCredentials.of(
                adminSeedProperties.username(),
                adminSeedProperties.password(),
                true));
        setField(adminUser, "info", new UserInfo(
                adminSeedProperties.fullName(),
                adminSeedProperties.email(),
                adminSeedProperties.address(),
                adminSeedProperties.phoneNumber()));
        setField(adminUser, "roles", new HashSet<>(Set.of(adminRole)));

        springDataUserRepository.save(adminUser);
    }

    private void seedRoles() {
        for (RoleName roleName : RoleName.values()) {
            springDataRoleRepository.findByName(roleName)
                    .orElseGet(() -> springDataRoleRepository.save(Role.builder().name(roleName).build()));
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Cannot set admin account field: " + fieldName, ex);
        }
    }
}


