package com.networknt.tram.todolist.command;

public class TodoNotFoundException extends Exception {
	public TodoNotFoundException(String id) {
		super(String.format("Todo with id=%s not found", id));
	}
}
