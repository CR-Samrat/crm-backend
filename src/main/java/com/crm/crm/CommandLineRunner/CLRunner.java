package com.crm.crm.CommandLineRunner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.crm.crm.model.Contacts;
import com.crm.crm.model.Todos;
import com.crm.crm.repository.ContactsRepository;
import com.crm.crm.repository.TodosRepository;

@Component
public class CLRunner implements CommandLineRunner{

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private TodosRepository todosRepository;

    @Override
    public void run(String... args) throws Exception {

    }
    
}
