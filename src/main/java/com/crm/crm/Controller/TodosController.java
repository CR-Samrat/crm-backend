package com.crm.crm.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.crm.Dto.TodoDto;
import com.crm.crm.model.Contacts;
import com.crm.crm.model.Todos;
import com.crm.crm.repository.ContactsRepository;
import com.crm.crm.repository.TodosRepository;
import com.crm.crm.service.TodosService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/todo")
@CrossOrigin
public class TodosController {

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private TodosRepository todosRepository;

    @Autowired
    private TodosService todosService;
    
    @PostMapping("/{id}/add")
    public ResponseEntity<?> addNewTodo(@PathVariable("id") Long id, @RequestBody TodoDto newTodo) {
        Optional<Contacts> contact = this.contactsRepository.findById(id);

        if(contact.isPresent()){
            return new ResponseEntity<>(this.todosService.addTodo(id, newTodo), HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Id doesn't exists", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/edit/{todoId}")
    public ResponseEntity<?> editTodo(@PathVariable("id") Long id, @PathVariable("todoId") Long todoId, @RequestBody TodoDto newTodo) {
        Optional<Contacts> contact = this.contactsRepository.findById(id);
        Optional<Todos> todo = this.todosRepository.findById(todoId);

        if(contact.isPresent() && todo.isPresent()){
            if(todo.get().getContact().getId().equals(id)){
                Todos modifiedTodo = this.todosService.editTodo(todo.get(), newTodo);

                return new ResponseEntity<>(this.contactsRepository.findById(id), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>("User doesn't have this todo", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Id doesn't exists", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/{id}/delete/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") Long id, @PathVariable("todoId") Long todoId) {
        Optional<Contacts> contact = this.contactsRepository.findById(id);

        if(contact.isPresent()){
            if(this.todosService.deleteTodo(contact.get(), todoId)){
                return new ResponseEntity<>(this.contactsRepository.findById(id), HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Todo Id doesn't exists", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Id doesn't exists", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/toggle/{todoId}")
    public ResponseEntity<?> toggleTodo(@PathVariable("id") Long id, @PathVariable("todoId") Long todoId) {
        Optional<Contacts> contact = this.contactsRepository.findById(id);
        
        if(contact.isPresent()){
            this.todosService.toggleTodo(todoId);
            return new ResponseEntity<>(this.contactsRepository.findById(id), HttpStatus.OK);
        }

        return new ResponseEntity<>("Id doesn't exists", HttpStatus.BAD_REQUEST);
    }
}
