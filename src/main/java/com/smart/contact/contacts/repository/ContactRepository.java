package com.smart.contact.contacts.repository;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("select c from Contact c where c.name like :name")
    public List<Contact> findByName(@Param("name") String name);

    @Query("select c from Contact c where c.contact1 like :contact or c.contact2 like :contact or c.contact3 like :contact")
    public List<Contact> findByContact(@Param("contact") String contact);

    @Query("select c from Contact c where c.nickname like :nickname")
    public List<Contact> findByNickname(@Param("nickname") String nickname);

    @Query("select c from Contact c where c.work like :work")
    public List<Contact> findByWork(@Param("work") String work);

    @Query("select c from Contact c inner join c.user where c.user.id = :id")
    public List<Contact> findByUser(@Param("id") long user);

    @Query("select c from Contact c inner join c.user where c.user.id = :id and c.name like :name")
    public List<Contact> findByUserAndName(@Param("id") long id, @Param("name") String name);

    @Query("select c from Contact c inner join c.user where c.user.id = :id and (c.contact1 like :contact or c.contact2 like :contact or c.contact3 like :contact)")
    public List<Contact> findByUserAndContact(@Param("id") long id, @Param("contact") String contact);

    @Query("select c from Contact c inner join c.user where c.user.id = :id and c.nickname like :nickname")
    public List<Contact> findByUserAndNickname(@Param("id") long id, @Param("nickname") String nickname);

    @Query("select c from Contact c inner join c.user where c.user.id = :id and c.work like :work")
    public List<Contact> findByUserAndWork(@Param("id") long id, @Param("work") String work);

    @Query("select count(c) from Contact c inner join c.user where c.user.id = :id")
    public long countByUser(@Param("id") long id);

    @Query("select count(c) from Contact c where c.date = :date")
    public long countByToday(@Param("date") String date);

    @Query("select count(c) from Contact c inner join c.user where c.user.id = :id and c.date = :date")
    public long countByUserAndToday(@Param("id") long id, @Param("date") String date);
}
