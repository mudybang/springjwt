package com.ombagoes.springrestjwt.user;

import com.ombagoes.springrestjwt.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password must fill.")
    private String password;

    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
