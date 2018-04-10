package com.networknt.subscription.schema;

import com.networknt.graphql.common.InstrumentationProvider;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;

import java.util.Collections;

/**
 * @author Nicholas Azar
 * Created on April 09, 2018
 */
public class SubscriptionInstrumentation implements InstrumentationProvider {
    @Override
    public Instrumentation getGraphqlInstrumentation() {
        return new ChainedInstrumentation(Collections.singletonList(new TracingInstrumentation()));
    }

    @Override
    public Instrumentation getGraphqlSubscriptionInstrumentation() {
        return null;
    }
}
