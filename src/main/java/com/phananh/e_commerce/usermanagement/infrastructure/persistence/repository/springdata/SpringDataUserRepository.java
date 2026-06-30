package com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByCredentials_Username(String username);

    boolean existsByCredentialsUsername(String username);

    boolean existsByInfoEmail(String email);

    boolean existsByRolesName(RoleName roleName);
}


