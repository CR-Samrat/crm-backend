package com.crm.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.crm.model.Contacts;
import java.util.List;


public interface ContactsRepository extends JpaRepository<Contacts, Long>{
    List<Contacts> findByEmail(String email);
    List<Contacts> findByUsername(String username);
}
