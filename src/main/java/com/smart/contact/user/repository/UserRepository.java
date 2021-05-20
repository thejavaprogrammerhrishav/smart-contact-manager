package com.smart.contact.user.repository;

import com.smart.contact.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String username);

    public List<User> findByName(String name);
}
