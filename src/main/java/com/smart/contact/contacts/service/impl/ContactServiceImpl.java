package com.smart.contact.contacts.service.impl;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.contacts.repository.ContactRepository;
import com.smart.contact.contacts.service.ContactService;
import com.smart.contact.user.model.User;
import com.smart.contact.util.DateDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository repository;

    @Override
    @Transactional
    public List<Contact> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Contact save(Contact user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<Contact> findById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public long count() {
        return repository.count();
    }
    @Override
    @Transactional
    public long countByToday() {
        return repository.countByToday(LocalDate.now().format(DateDto.FORMAT));
    }
    @Override
    @Transactional
    public long countByUserAndToday(User user) {
        return repository.countByUserAndToday(user.getId(),LocalDate.now().format(DateDto.FORMAT));
    }

    @Override
    @Transactional
    public long countByUser(User user) {
        return repository.countByUser(user.getId());
    }

    @Override
    @Transactional
    public long countByWeek(DateDto dto) {
        return repository.findAll().parallelStream().filter(f -> {
            LocalDate date = LocalDate.parse(f.getDate(), DateDto.FORMAT);
            return !date.isBefore(dto.getStart()) && !date.isAfter(dto.getEnd());
        }).count();
    }

    @Override
    @Transactional
    public long countByMonth(DateDto dto) {
        return repository.findAll().parallelStream().filter(f -> {
            LocalDate date = LocalDate.parse(f.getDate(), DateDto.FORMAT);
            return !date.isBefore(dto.getStart()) && !date.isAfter(dto.getEnd());
        }).count();
    }

    @Override
    @Transactional
    public long countByUserAndWeek(User user, DateDto dto) {
        return repository.findByUser(user.getId()).parallelStream().filter(f -> {
            LocalDate date = LocalDate.parse(f.getDate(), DateDto.FORMAT);
            return !date.isBefore(dto.getStart()) && !date.isAfter(dto.getEnd());
        }).count();
    }

    @Override
    @Transactional
    public long countByUserAndMonth(User user, DateDto dto) {
        return repository.findByUser(user.getId()).parallelStream().filter(f -> {
            LocalDate date = LocalDate.parse(f.getDate(), DateDto.FORMAT);
            return !date.isBefore(dto.getStart()) && !date.isAfter(dto.getEnd());
        }).count();
    }

    @Override
    @Transactional
    public void delete(Contact user) {
        repository.delete(user);
    }

    @Override
    @Transactional
    public List<Contact> findByName(String name) {
        return repository.findByName("%" + name + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByContact(String contact) {
        return repository.findByContact("%" + contact + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByNickname(String nickname) {
        return repository.findByNickname("%" + nickname + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByWork(String work) {
        return repository.findByWork("%" + work + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByUser(User user) {
        return repository.findByUser(user.getId());
    }

    @Override
    @Transactional
    public List<Contact> findByUserAndName(User user, String name) {
        return repository.findByUserAndName(user.getId(), "%" + name + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByUserAndContact(User user, String contact) {
        return repository.findByUserAndContact(user.getId(), "%" + contact + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByUserAndNickname(User user, String nickname) {
        return repository.findByUserAndNickname(user.getId(), "%" + nickname + "%");
    }

    @Override
    @Transactional
    public List<Contact> findByUserAndWork(User user, String work) {
        return repository.findByUserAndWork(user.getId(), "%" + work + "%");
    }
}
