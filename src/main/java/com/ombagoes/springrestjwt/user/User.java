package com.ombagoes.springrestjwt.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;

    @JsonIgnoreProperties("users")//avoid looping
    @ManyToOne(cascade = CascadeType.ALL)
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
