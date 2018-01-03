
package com.networknt.starwars.schema;

import com.networknt.graphql.router.SchemaProvider;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by steve on 25/03/17.
 */
public class StarWarsSchema implements SchemaProvider {
    private static Logger logger = LoggerFactory.getLogger(SchemaProvider.class);
    private static String schemaName = "schema.graphqls";
    @Override
    public GraphQLSchema getSchema() {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeRegistry = null;

        try(InputStream is = getClass().getClassLoader().getResourceAsStream(schemaName)) {
            typeRegistry = schemaParser.parse(new InputStreamReader(is));
        } catch (IOException e) {
            logger.error("IOException:", e);
        }

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
            // put other wiring logic here.
            .build();

        return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
    }
}
