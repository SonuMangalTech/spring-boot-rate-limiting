package com.sonumangal.tech.entity;

import com.sonumangal.tech.model.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "user_details",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"userKey", "role"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String userKey;

    @Enumerated(EnumType.STRING)
    private ROLE role = ROLE.GUEST;

    @OneToMany(
            mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ApiEntity> apiEntities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public List<ApiEntity> getApiEntities() {
        return apiEntities;
    }

    public void setApiEntities(List<ApiEntity> apiEntities) {
        this.apiEntities = apiEntities;
    }
}
