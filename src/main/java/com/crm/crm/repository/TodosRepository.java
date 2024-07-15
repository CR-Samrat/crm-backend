package com.crm.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.crm.model.Contacts;
import com.crm.crm.model.Todos;
import java.util.List;


public interface TodosRepository extends JpaRepository<Todos, Long>{
    List<Todos> findByContact(Contacts contact);
}
