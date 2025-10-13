package com.authentication.system.security_project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.authentication.system.security_project.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(Set.of(
            USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE
    )),

    MANAGER(Set.of(
            USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE,
            MANAGER_CREATE, MANAGER_READ, MANAGER_UPDATE, MANAGER_DELETE
    )),

    ADMIN(Set.of(
            USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE,
            MANAGER_CREATE, MANAGER_READ, MANAGER_UPDATE, MANAGER_DELETE,
            ADMIN_CREATE, ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE
    ));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(permissions.stream()
                                                                              .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName())).toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
