package com.bm3.accounting.security;

import com.bm3.accounting.entity.User;
import com.bm3.accounting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("permissionService")
@RequiredArgsConstructor
public class PermissionService {
    private final UserRepository userRepository;

    /*
    ('USER_MANAGEMENT', 'CREATE')
    must be
    ('Accounting', 'User_Management')
     */
    /**
     * este metodo pregunta que permisos tiene un usuario para un modulo determinado.
     *
     * @param moduleName possibles values Accounting or Marketing or banking
     * @param permissionName type or permission Accounting_delete or User_Management
     * @return  true if the user has permission, false otherwise.
     */
    public boolean hasPermission(String moduleName, String permissionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        //User user = userRepository.findByUsername(username).orElse(null);

        User user = userRepository.findByUsernameWithRolesAndPermissionsAndModules(username)
                .orElse(null);

        if (user == null) {
            return false;
        }

        return user.getRolesMtM().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission ->
                        permission.getModule().getDescription().equals(moduleName) &&
                                permission.getName().equals(permissionName)
                );
    }

    /**
     *
     * @param roleName possible values. ‘USER’, 'ADMIN'
     * @return true if the user has role allowed, false otherwise.
     */
    public boolean hasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + roleName));
    }
}
