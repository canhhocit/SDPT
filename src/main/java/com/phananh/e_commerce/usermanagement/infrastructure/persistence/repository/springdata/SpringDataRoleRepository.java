package com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SpringDataRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
    Set<Role> findAllByNameIn(Set<RoleName> roleNames);
}


