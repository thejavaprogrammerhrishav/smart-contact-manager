package com.smart.contact.user.service.impl;

import com.smart.contact.user.model.User;
import com.smart.contact.user.repository.UserRepository;
import com.smart.contact.user.service.UserService;
import com.smart.contact.util.DateDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> findById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public long count() {
        return repository.count();
    }


    @Override
    @Transactional
    public long countByDate(String from, String end) {
        LocalDate fromD = LocalDate.parse(from, DateDto.FORMAT);
        LocalDate toD = LocalDate.parse(end, DateDto.FORMAT);

        return repository.findAll().parallelStream()
                .filter(f -> {
                    LocalDate date = LocalDate.parse(f.getDate(), DateDto.FORMAT);
                    return !date.isBefore(fromD) && !date.isAfter(toD);
                }).count();
    }

    @Override
    @Transactional
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public List<User> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<User> findByDate(String from, String end) {
        LocalDate fromD = LocalDate.parse(from, DateDto.FORMAT);
        LocalDate toD = LocalDate.parse(end, DateDto.FORMAT);

        return repository.findAll().parallelStream()
                .filter(f -> {
                    LocalDate date = LocalDate.parse(f.getDate(), DateDto.FORMAT);
                    return !date.isBefore(fromD) && !date.isAfter(toD);
                }).sorted(Comparator.comparingLong(User::getId)).collect(Collectors.toList());
    }
}
