package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.domain.repository.UserRepository;
import org.FelipeBert.ecommerce.service.exception.BusinessException;
import org.FelipeBert.ecommerce.service.exception.EntityAlreadyExistsException;
import org.FelipeBert.ecommerce.service.exception.IdsMustBeEqualsException;
import org.FelipeBert.ecommerce.service.exception.NotFoundException;
import org.FelipeBert.ecommerce.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    public void testCreateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);
        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
    }

    @Test
    public void testCreateUserAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistsException.class, () -> userService.create(user));
    }

    @Test
    public void testUpdatePassword() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User updatedUser = new User();
        updatedUser.setPassword("newPassword");

        Mockito.when(userRepository.save(user)).thenReturn(updatedUser);

        User result = userService.updatePassword(1L, 1L, "password", "newPassword");
        assertEquals("newPassword", result.getPassword());
    }

    @Test
    public void testUpdatePasswordIncorrect() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> userService.updatePassword(1L, 1L, "wrongPassword", "newPassword"));
    }

    @Test
    public void testUpdatePasswordSameAsCurrent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> userService.updatePassword(1L, 1L, "password", "password"));
    }

    @Test
    public void testUpdatePasswordIdsNotEqual() {
        assertThrows(IdsMustBeEqualsException.class, () -> userService.updatePassword(1L, 2L, "password", "newPassword"));
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setUsername("newUsername");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        User result = userService.update(user.getId(), updatedUser);

        assertEquals("newUsername", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUser");

        assertThrows(NotFoundException.class, () -> userService.update(1L, updatedUser));
    }

    @Test
    public void testUpdateUserIdsNotEqual() {
        User updatedUser = new User();
        updatedUser.setId(2L);
        updatedUser.setUsername("updatedUser");

        assertThrows(IdsMustBeEqualsException.class, () -> userService.update(1L, updatedUser));
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.delete(1L));
    }
}
