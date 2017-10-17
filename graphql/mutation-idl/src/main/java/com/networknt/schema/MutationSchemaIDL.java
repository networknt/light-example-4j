package com.networknt.schema;

import com.networknt.config.Config;
import com.networknt.graphql.router.SchemaProvider;
import graphql.schema.*;
import graphql.schema.idl.*;

/**
 * Created by Nicholas Azar on October 16, 2017.
 */
public class MutationSchemaIDL implements SchemaProvider {
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

    @Override
    public GraphQLSchema getSchema() {
        Config config = Config.getInstance();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(config.getStringFromFile("mutation-schema.graphqls"));
        RuntimeWiring wiring = buildRuntimeWiring();
        return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, wiring);
    }
}
