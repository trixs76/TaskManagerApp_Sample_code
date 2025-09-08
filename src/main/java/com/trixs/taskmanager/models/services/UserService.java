package com.trixs.taskmanager.models.services;

import com.trixs.taskmanager.models.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void create(UserDTO user, boolean isAdmin);
}
