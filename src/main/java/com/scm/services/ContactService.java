package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
    // save contact
    Contact save(Contact contact);

    //update contact
    Contact update(Contact contact);

    //get contacts

    List<Contact>getAll();

    //get contact by id

    Contact getById(String id);

    //delete Contact
    void delete(String id);

    //search Contact
    Page<Contact> searchByName(String nameKeyword,int size,int page, String sortBy,String order,User user);
    Page<Contact> searchByEmail(String emailKeyword , int size,int page, String sortBy,String order,User user);
    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword , int size,int page, String sortBy,String order,User user);

    //get contact by userId

    List<Contact>getByUserId(String userId);

    Page<Contact>getByUser(User user,int page,int size,String sortField,String direction);
}
