package org.FelipeBert.ecommerce.domain.repository;

import org.FelipeBert.ecommerce.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}