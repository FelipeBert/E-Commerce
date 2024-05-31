package org.FelipeBert.ecommerce.controller.dto;

import org.FelipeBert.ecommerce.domain.model.User;

public record UserCreateDTO(Long id,
                            String username,
                            String fullname,
                            String email,
                            String login,
                            String password,
                            double amount) {
    public User dtoToUser(){
        User model = new User();
        model.setId(this.id);
        model.setUsername(this.username);
        model.setFullname(this.fullname);
        model.setEmail(this.email);
        model.setLogin(this.login);
        model.setPassword(this.password);
        model.setAmount(this.amount);
        return model;
    }
}
