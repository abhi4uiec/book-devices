package com.phone.devices.repo;

import com.phone.devices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing user entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findByUsername(String username);
}

