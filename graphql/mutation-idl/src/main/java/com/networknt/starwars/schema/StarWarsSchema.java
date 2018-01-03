
package com.networknt.starwars.schema;

import com.networknt.graphql.router.SchemaProvider;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Nicholas Azar on October 16, 2017.
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

        RuntimeWiring wiring = buildRuntimeWiring();
        return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("numberHolder", MutationWiring.numberHolderFetcher))
                .type(TypeRuntimeWiring.newTypeWiring("theNumber")
                        .dataFetcher("theNumber", MutationWiring.theNumberFetcher))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                        .dataFetcher("changeTheNumber", MutationWiring.changeTheNumberFetcher)
                        .dataFetcher("failToChangeTheNumber", MutationWiring.failToChangeTheNumberFetcher)
                )
                .build();
    }

}
