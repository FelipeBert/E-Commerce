package org.FelipeBert.ecommerce.controller.dto;

import org.FelipeBert.ecommerce.domain.model.User;

public record UserDTO(String fullname, String username, Long id, String email) {
    public UserDTO(User user){
        this(   user.getFullname(),
                user.getUsername(),
                user.getId(),
                user.getEmail());
    }
}
