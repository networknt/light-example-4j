
package com.networknt.starwars.schema;

import com.networknt.graphql.router.SchemaProvider;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * Created by steve on 25/03/17.
 */
public class StarWarsSchema implements SchemaProvider {
    @Override
    public GraphQLSchema getSchema() {
        GraphQLObjectType queryType = newObject()
                .name("helloWorldQuery")
                .field(newFieldDefinition()
                        .type(GraphQLString)
                        .name("hello")
                        .staticValue("world"))
                .build();

        return GraphQLSchema.newSchema()
                .query(queryType)
                .build();
    }
}

