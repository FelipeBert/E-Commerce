package org.FelipeBert.ecommerce.controller;

import org.FelipeBert.ecommerce.controller.dto.UserCreateDTO;
import org.FelipeBert.ecommerce.controller.dto.UserDTO;
import org.FelipeBert.ecommerce.controller.dto.UserUpdateDTO;
import org.FelipeBert.ecommerce.controller.dto.UserUpdatePasswordDTO;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public record UserController(UserService userService) {

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(){
        var users = userService.findAll();
        var usersDTO = users.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(usersDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(new UserDTO(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserCreateDTO createDTO){
        User createdUser = createDTO.dtoToUser();
        userService.create(createdUser);
        return ResponseEntity.ok(new UserDTO(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO){
        User user = userService.findById(id);
        user.setUsername(updateDTO.username());
        userService.update(id, user);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable Long id, @RequestBody UserUpdatePasswordDTO updateDTO){
        User user = userService.updatePassword(id,
                                updateDTO.entityId(),
                                updateDTO.currentPassword(),
                                updateDTO.newPassword());
        return ResponseEntity.ok(new UserDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
