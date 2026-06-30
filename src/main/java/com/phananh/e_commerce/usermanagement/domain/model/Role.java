package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private RoleName name;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    private Set<User> users = new HashSet<>();
}


