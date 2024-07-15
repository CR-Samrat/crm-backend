package com.crm.crm.service;

import com.crm.crm.Dto.TodoDto;
import com.crm.crm.model.Contacts;
import com.crm.crm.model.Todos;

public interface TodosService {
    Contacts addTodo(Long id, TodoDto newTodo);
    boolean deleteTodo(Contacts contact, Long todoId);
    boolean toggleTodo(Long todoId);
    Todos editTodo(Todos exTodo, TodoDto newTodo);
}
