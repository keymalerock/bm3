package com.bm3.accounting.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idUser;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 60)
    private String email;

    @Column(name = "password", nullable = false)
    private String key;

    @Column(name = "is_enable")
    private boolean isEnable;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Rol> rolesMtM = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
}
