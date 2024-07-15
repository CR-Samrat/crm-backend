package com.crm.crm.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crm.crm.Dto.TodoDto;
import com.crm.crm.model.Contacts;
import com.crm.crm.model.Todos;
import com.crm.crm.repository.ContactsRepository;
import com.crm.crm.repository.TodosRepository;
import com.crm.crm.service.TodosService;

@Service
@Transactional
public class TodosServiceImpl implements TodosService{

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private TodosRepository todosRepository;

    @Override
    public Contacts addTodo(Long id, TodoDto newTodo) {
        Contacts contact = this.contactsRepository.findById(id).get();

        Todos todo = new Todos(contact, newTodo.isEnable(), newTodo.getDescription());

        contact.getTodos().add(todo);

        return this.contactsRepository.save(contact);
    }

    @Override
    public boolean deleteTodo(Contacts contact , Long todoId) {

        Optional<Todos> optTodo = this.todosRepository.findById(todoId);

        if(optTodo.isPresent()){
            Todos todo = optTodo.get();

            if(todo.getContact().getId().equals(contact.getId())){
                this.todosRepository.delete(todo);

                contact.getTodos().remove(todo);

                this.contactsRepository.save(contact);

                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public boolean toggleTodo(Long todoId) {
        Todos todo = this.todosRepository.findById(todoId).get();

        todo.setEnable(!todo.isEnable());

        return true;
    }

    @Override
    public Todos editTodo(Todos exTodo, TodoDto newTodo) {
        exTodo.setDescription(newTodo.getDescription());
        exTodo.setEnable(exTodo.isEnable());

        return this.todosRepository.save(exTodo);
    }
    
}
