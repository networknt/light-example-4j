package com.networknt.schema;

import graphql.relay.Edge;
import graphql.schema.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;

/**
 * Created by steve on 26/03/17.
 */
public class TodoSchemaMutations {

    private GraphQLFieldDefinition addTodo;
    private GraphQLFieldDefinition changeStatus;
    private GraphQLFieldDefinition markAll;
    private GraphQLFieldDefinition removeCompleted;
    private GraphQLFieldDefinition removeTodo;
    private GraphQLFieldDefinition renameTodo;

    private TodoSchema todoSchema;


    public TodoSchemaMutations(TodoSchema todoSchema) {
        this.todoSchema = todoSchema;

        createAddTodoMutation();
        createChangeTodoStatusMutation();
        createMarkAllTodosMutation();
        createRemoveCompletedTodosMutation();
        createRemoveTodoMutation();
        createRenameTodoMutation();
    }

    private GraphQLFieldDefinition getViewerField() {
        GraphQLFieldDefinition viewer = newFieldDefinition()
                .name("viewer")
                .type(todoSchema.getUserType())
                .staticValue(todoSchema.getTheOnlyUser())
                .build();
        return viewer;
    }

    public List<GraphQLFieldDefinition> getFields() {
        return Arrays.asList(addTodo, changeStatus, markAll, removeCompleted, removeTodo, renameTodo);
    }


    private void createAddTodoMutation() {
        GraphQLInputObjectField textField = newInputObjectField()
                .name("text")
                .type(new GraphQLNonNull(GraphQLString))
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(textField);

        GraphQLFieldDefinition todoEdge = newFieldDefinition()
                .name("todoEdge")
                .type(todoSchema.getTodosEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String todoId = (String) source.get("todoId");
                    Todo todo = todoSchema.getTodo(todoId);
                    return new Edge(todo, todoSchema.getSimpleConnection().cursorForObjectInConnection(todo));
                })
                .build();


        List<GraphQLFieldDefinition> outputFields = Arrays.asList(todoEdge, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String text = (String) input.get("text");
            String newId = todoSchema.addTodo(text);
            Map<String, String> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("todoId", newId);
            return result;
        };


        addTodo = todoSchema.getRelay().mutationWithClientMutationId("AddTodo", "addTodo", inputFields, outputFields, mutate);
    }

    private void createChangeTodoStatusMutation() {
        GraphQLInputObjectField completeField = newInputObjectField()
                .name("complete")
                .type(new GraphQLNonNull(GraphQLBoolean))
                .build();
        GraphQLInputObjectField idField = newInputObjectField()
                .name("id")
                .type(new GraphQLNonNull(GraphQLID))
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(completeField, idField);

        GraphQLFieldDefinition todoField = newFieldDefinition()
                .name("todo")
                .type(todoSchema.getTodoType())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String todoId = (String) source.get("todoId");
                    Todo todo = todoSchema.getTodo(todoId);
                    return todo;
                })
                .build();

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(todoField, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String id = (String) input.get("id");
            boolean complete = (boolean) input.get("complete");
            String localId = todoSchema.getRelay().fromGlobalId(id).id;
            todoSchema.changeTodoStatus(localId, complete);
            Map<String, String> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("todoId", localId);
            return result;
        };


        changeStatus = todoSchema.getRelay().mutationWithClientMutationId("ChangeTodoStatus", "changeTodoStatus", inputFields, outputFields, mutate);
    }

    private void createMarkAllTodosMutation() {
        GraphQLInputObjectField completeField = newInputObjectField()
                .name("complete")
                .type(new GraphQLNonNull(GraphQLBoolean))
                .build();
        List<GraphQLInputObjectField> inputFields = Arrays.asList(completeField);

        GraphQLFieldDefinition changedTodos = newFieldDefinition()
                .name("changedTodos")
                .type(new GraphQLList(todoSchema.getTodoType()))
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    return todoSchema.getTodos((List<String>) source.get("todIds"));
                })
                .build();


        List<GraphQLFieldDefinition> outputFields = Arrays.asList(changedTodos, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            boolean complete = (boolean) input.get("complete");
            List<String> ids = todoSchema.markAllTodos(complete);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("todIds", ids);
            return result;
        };


        markAll = todoSchema.getRelay().mutationWithClientMutationId("MarkAllTodos", "markAllTodos", inputFields, outputFields, mutate);
    }

    private void createRemoveCompletedTodosMutation() {
        List<GraphQLInputObjectField> inputFields = Arrays.asList();

        GraphQLFieldDefinition todoEdge = newFieldDefinition()
                .name("deletedTodoIds")
                .type(new GraphQLList(GraphQLString))
                .build();

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(todoEdge, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            List<String> deletedTodoIds = todoSchema.removeCompletedTodos();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("deletedTodoIds", deletedTodoIds.stream().map(s -> todoSchema.getRelay().toGlobalId("Todo",s)).collect(Collectors.toList()));
            return result;
        };


        removeCompleted = todoSchema.getRelay().mutationWithClientMutationId("RemoveCompletedTodos", "removeCompletedTodos", inputFields, outputFields, mutate);
    }

    private void createRemoveTodoMutation() {
        GraphQLInputObjectField idField = newInputObjectField()
                .name("id")
                .type(new GraphQLNonNull(GraphQLID))
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(idField);

        GraphQLFieldDefinition todoEdge = newFieldDefinition()
                .name("deletedTodoId")
                .type(GraphQLID)
                .build();

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(todoEdge, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String id = (String) input.get("id");
            String localId = todoSchema.getRelay().fromGlobalId(id).id;
            todoSchema.removeTodo(localId);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("deletedTodoId", id);
            return result;
        };


        removeTodo = todoSchema.getRelay().mutationWithClientMutationId("RemoveTodo", "removeTodo", inputFields, outputFields, mutate);
    }

    private void createRenameTodoMutation() {
        GraphQLInputObjectField idField = newInputObjectField()
                .name("id")
                .type(new GraphQLNonNull(GraphQLID))
                .build();
        GraphQLInputObjectField textField = newInputObjectField()
                .name("text")
                .type(new GraphQLNonNull(GraphQLString))
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(idField, textField);

        GraphQLFieldDefinition todoField = newFieldDefinition()
                .name("todo")
                .type(todoSchema.getTodoType())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String todoId = (String) source.get("localId");
                    Todo todo = todoSchema.getTodo(todoId);
                    return todo;
                })
                .build();

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(todoField, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String id = (String) input.get("id");
            String text = (String) input.get("text");
            String localId = todoSchema.getRelay().fromGlobalId(id).id;
            todoSchema.renameTodo(localId, text);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("localId", localId);
            return result;
        };


        renameTodo = todoSchema.getRelay().mutationWithClientMutationId("RenameTodo", "renameTodo", inputFields, outputFields, mutate);
    }


}
