package com.smart.contact.user.service;

import com.smart.contact.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> findAll();

    public User save(User user);

    public Optional<User> findById(long id);

    public long count();

    public long countByDate(String from, String end);

    public void delete(User user);

    public Optional<User> findByUsername(String username);

    public List<User> findByName(String name);

    public List<User> findByDate(String from, String end);
}
