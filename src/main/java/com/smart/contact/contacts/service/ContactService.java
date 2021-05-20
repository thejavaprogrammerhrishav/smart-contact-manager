package com.smart.contact.contacts.service;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.user.model.User;
import com.smart.contact.util.DateDto;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


public interface ContactService {
    public List<Contact> findAll();

    public Contact save(Contact user);

    public Optional<Contact> findById(long id);

    public long count();

    public long countByToday();

    public long countByUserAndToday(User user);

    public long countByUser(User user);

    public long countByWeek(DateDto dto);

    public long countByMonth(DateDto dto);

    public long countByUserAndWeek(User user, DateDto dto);

    public long countByUserAndMonth(User user, DateDto dto);

    public void delete(Contact user);

    public List<Contact> findByName(String name);

    public List<Contact> findByContact(String contact);

    public List<Contact> findByNickname(String nickname);

    public List<Contact> findByWork(String work);

    public List<Contact> findByUser(User user);

    public List<Contact> findByUserAndName(User user, String name);

    public List<Contact> findByUserAndContact(User user, String contact);

    public List<Contact> findByUserAndNickname(User user, String nickname);

    public List<Contact> findByUserAndWork(User user, String work);
}
