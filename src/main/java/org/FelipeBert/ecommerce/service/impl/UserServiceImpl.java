package org.FelipeBert.ecommerce.service.impl;

import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.domain.repository.UserRepository;
import org.FelipeBert.ecommerce.service.UserService;
import org.FelipeBert.ecommerce.service.exception.BusinessException;
import org.FelipeBert.ecommerce.service.exception.EntityAlreadyExistsException;
import org.FelipeBert.ecommerce.service.exception.IdsMustBeEqualsException;
import org.FelipeBert.ecommerce.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public User create(User entity) {
        Optional<User> checkExists = userRepository.findById(entity.getId());
        if(checkExists.isPresent()){
            throw new EntityAlreadyExistsException("User");
        }
        return userRepository.save(entity);
    }

    @Transactional
    public User updatePassword(Long id, Long entityId, String currentPassword, String newPassword) {
        if(!id.equals(entityId)){
            throw new IdsMustBeEqualsException();
        }
        Optional<User> checkIfUserExists = userRepository.findById(id);
        User user = checkIfUserExists.orElseThrow(NotFoundException::new);
        if(currentPassword.equals(newPassword) || !currentPassword.equals(user.getPassword())){
            throw new BusinessException("Incorret Password");
        }
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User entity) {
        if(!id.equals(entity.getId())){
            throw new IdsMustBeEqualsException();
        }
        Optional<User> checkIfUserExists = userRepository.findById(id);
        User user = checkIfUserExists.orElseThrow(NotFoundException::new);
        user.setUsername(entity.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        Optional<User> checkIfUserExists = userRepository.findById(id);
        User userToDelete = checkIfUserExists.orElseThrow(NotFoundException::new);
        userRepository.delete(userToDelete);
    }
}
