package com.antipin.oauth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Access(AccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", initialValue = 100, allocationSize = 1)
    private Long id;

    @Column
    @NotNull
    private String role;

    @Override
    public String getAuthority() {
        return "ROLE_" + getRole();
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Role r) {
            return this.role.equals(r.getRole());
        }
        return false;
    }
}
