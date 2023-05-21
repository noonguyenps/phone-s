package project.phoneshop.common;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static project.phoneshop.common.UserPermission.*;

public enum AppUserRole {
    USER(Sets.newHashSet(USER_READ, USER_WRITE)),
    ADMIN(Sets.newHashSet(ADMIN_READ, ADMIN_WRITE, USER_READ, USER_WRITE,MANAGER_READ, MANAGER_WRITE)),
    MANAGER(Sets.newHashSet(MANAGER_READ, MANAGER_WRITE, USER_READ, USER_WRITE)),
    SHIPPER(Sets.newHashSet(SHIPPER_READ,SHIPPER_WRITE));

    private final Set<UserPermission> permissions;

    AppUserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        return permissions;
    }
}