package com.assignment.bitespeed.Repository;

import com.assignment.bitespeed.Entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer>
{
    @Query(nativeQuery = true,value = "select * from contact where email=? limit 1")
    Contact findByEmail(String email);
    @Query(nativeQuery = true,value = "select * from contact where phone_number=? limit 1")
    Contact findByPhoneNumber(String phonenumber);

    Contact findByEmailAndLinkPrecedence(String email,String precedence);

    Contact findByPhoneNumberAndLinkPrecedence(String phnumber,String precedence);

    @Query(nativeQuery = true,value = "select distinct email from contact where linked_id=:id or id=:id and email is not null")
    List<String> findEmailByLinkedId(int id);

    @Query(nativeQuery = true,value = "select id from contact where linked_id=:id")
    List<String> findSecId(int id);


    @Query(nativeQuery = true,value = "select distinct phone_number from contact where linked_id=:id or id=:id and phone_number is not null")
    List<String> findPhoneNumbers(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update contact set linked_id=:id where linked_id=:id1")
    void update(int id, int id1);
}
