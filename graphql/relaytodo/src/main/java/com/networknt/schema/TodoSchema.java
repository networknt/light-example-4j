package com.networknt.subscription.schema;

import com.networknt.graphql.router.SchemaProvider;
import graphql.Scalars;
import graphql.relay.Connection;
import graphql.relay.DefaultEdge;
import graphql.relay.Relay;
import graphql.relay.SimpleListConnection;
import graphql.schema.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLSchema.newSchema;

/**
 * Created by steve on 25/03/17.
 */
public class TodoSchema implements SchemaProvider {
    private GraphQLObjectType todoType;

    private GraphQLObjectType userType;

    private GraphQLObjectType connectionFromUserToTodos;
    private GraphQLInterfaceType nodeInterface;


    private GraphQLObjectType todosEdge;


    private User theOnlyUser = new User();
    private List<Todo> todos = new ArrayList<>();


    private SimpleListConnection simpleConnection;


    private Relay relay = new Relay();

    int nextTodoId = 0;

    @Override
    public GraphQLSchema getSchema() {
        addTodo("Do Something");
        addTodo("Other todo");

        TypeResolverProxy typeResolverProxy = new TypeResolverProxy();
        nodeInterface = relay.nodeInterface(typeResolverProxy);
        simpleConnection = new SimpleListConnection(todos);

        createTodoType();
        createConnectionFromUserToTodos();
        createUserType();

        DataFetcher todoDataFetcher = environment -> {
            String id = environment.getArgument("id");
            return new User();
        };

        GraphQLObjectType QueryRoot = newObject()
                .name("Root")
                .field(newFieldDefinition()
                        .name("viewer")
                        .type(userType)
                        .staticValue(theOnlyUser)
                        .build())
                .field(relay.nodeField(nodeInterface, todoDataFetcher))
                .build();

        TodoSchemaMutations todoSchemaMutations = new TodoSchemaMutations(this);

        GraphQLObjectType mutationType = newObject()
                .name("Mutation")
                .fields(todoSchemaMutations.getFields())
                .build();

        return newSchema()
                .query(QueryRoot)
                .mutation(mutationType)
                .build();
    }

    private void createUserType() {
        userType = newObject()
                .name("User")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    User user = (User) environment.getSource();
                                    return relay.toGlobalId("User", user.getId());
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("todos")
                        .type(connectionFromUserToTodos)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(simpleConnection)
                        .build())
                .withInterface(nodeInterface)
                .build();
    }

    private void createTodoType() {
        todoType = newObject()
                .name("Todo")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    Todo todo = (Todo) environment.getSource();
                                    return relay.toGlobalId("Todo", todo.getId());
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("text")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("complete")
                        .type(Scalars.GraphQLBoolean)
                        .build())
                .withInterface(nodeInterface)
                .build();
    }

    private void createConnectionFromUserToTodos() {
        todosEdge = relay.edgeType("Todo", todoType, nodeInterface, Collections.<GraphQLFieldDefinition>emptyList());
        GraphQLFieldDefinition totalCount = newFieldDefinition()
                .name("totalCount")
                .type(GraphQLInt)
                .dataFetcher(environment -> {
                    Connection connection = (Connection) environment.getSource();
                    return connection.getEdges().size();
                })
                .build();

        GraphQLFieldDefinition completedCount = newFieldDefinition()
                .name("completedCount")
                .type(GraphQLInt)
                .dataFetcher(environment -> {
                    Connection connection = (Connection) environment.getSource();
                    return (int) connection.getEdges().stream().filter(edge -> ((Todo)((DefaultEdge)edge).getNode()).isComplete()).count();
                })
                .build();
        connectionFromUserToTodos = relay.connectionType("Todo", todosEdge, Arrays.asList(totalCount, completedCount));
    }

    public User getTheOnlyUser() {
        return theOnlyUser;
    }


    public SimpleListConnection getSimpleConnection() {
        return simpleConnection;
    }


    public GraphQLObjectType getUserType() {
        return userType;
    }

    public GraphQLObjectType getTodoType() {
        return todoType;
    }

    public GraphQLObjectType getTodosEdge() {
        return todosEdge;
    }


    public Relay getRelay() {
        return relay;
    }


    public String addTodo(String text) {
        Todo newTodo = new Todo();
        newTodo.setId(Integer.toString(nextTodoId++));
        newTodo.setText(text);
        todos.add(newTodo);
        return newTodo.getId();
    }


    public void removeTodo(String id) {
        Todo del = todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        todos.remove(del);
    }

    public void renameTodo(String id, String text) {
        Todo matchedTodo = todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        matchedTodo.setText(text);
    }


    public List<String> removeCompletedTodos() {
        List<String> toDelete = todos.stream().filter(Todo::isComplete).map(Todo::getId).collect(Collectors.toList());
        todos.removeIf(todo -> toDelete.contains(todo.getId()));
        return toDelete;
    }

    public List<String> markAllTodos(boolean complete) {
        List<String> changed = new ArrayList<>();
        todos.stream().filter(todo -> complete != todo.isComplete()).forEach(todo -> {
            changed.add(todo.getId());
            todo.setComplete(complete);
        });

        return changed;
    }

    public void changeTodoStatus(String id, boolean complete) {
        Todo todo = getTodo(id);
        todo.setComplete(complete);
    }


    public Todo getTodo(String id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
    }

    public List<Todo> getTodos(List<String> ids) {
        return todos.stream().filter(todo -> ids.contains(todo.getId())).collect(Collectors.toList());
    }

}
