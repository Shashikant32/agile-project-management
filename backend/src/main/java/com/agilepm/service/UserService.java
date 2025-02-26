package com.agilepm.service;

import com.agilepm.dto.UserDTO;
import com.agilepm.model.User;
import com.agilepm.security.Permission;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface UserService {
    @PreAuthorize("hasPermission(null, 'USER_WRITE')")
    UserDTO createUser(UserDTO userDTO);

    @PreAuthorize("hasPermission(null, 'USER_WRITE')")
    UserDTO updateUser(Long userId, UserDTO userDTO);

    @PreAuthorize("hasPermission(null, 'USER_DELETE')")
    void deleteUser(Long userId);

    @PreAuthorize("hasPermission(null, 'USER_READ')")
    UserDTO getUserById(Long userId);

    @PreAuthorize("hasPermission(null, 'USER_READ')")
    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByCompany(Long companyId);
    boolean existsByEmail(String email);

    @PreAuthorize("hasPermission(null, 'USER_WRITE')")
    UserDTO assignUserToCompany(Long userId, Long companyId);
}
