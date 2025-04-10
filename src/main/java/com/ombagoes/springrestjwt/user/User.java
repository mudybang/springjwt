package com.ombagoes.springrestjwt.user;

import com.ombagoes.springrestjwt.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "[a-zA-Z\\s]+$", message = "invalid name, char only")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password must fill.")
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    public void addTimestamp() {
        createdAt = new Date();
    }

    @Column(name = "updated_at")
    private Date updatedAt;

    @PreUpdate
    public void updateTimestamp() {
        updatedAt = new Date();
    }
}
