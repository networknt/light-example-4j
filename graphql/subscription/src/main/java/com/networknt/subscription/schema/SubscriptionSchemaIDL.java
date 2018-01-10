package com.networknt.subscription.schema;

import com.networknt.config.Config;
import com.networknt.graphql.router.SchemaProvider;
import com.networknt.subscription.schema.SubscriptionWiring;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;


/**
 * Created by Nicholas Azar on October 18, 2017.
 */
public class SubscriptionSchemaIDL implements SchemaProvider {

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("channels", SubscriptionWiring.channelsFetcher)
                        .dataFetcher("channel", SubscriptionWiring.channelFetcher))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                        .dataFetcher("addChannel", SubscriptionWiring.addChannelFetcher)
                        .dataFetcher("addMessage", SubscriptionWiring.addMessageFetcher))
                .type(TypeRuntimeWiring.newTypeWiring("Subscription")
                        .dataFetcher("messageAdded", SubscriptionWiring.messageAddedFetcher))
                .build();
    }

    @Override
    public GraphQLSchema getSchema() {
        Config config = Config.getInstance();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(config.getStringFromFile("subscription-schema.graphqls"));
        RuntimeWiring wiring = buildRuntimeWiring();
        return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, wiring);
    }
}
