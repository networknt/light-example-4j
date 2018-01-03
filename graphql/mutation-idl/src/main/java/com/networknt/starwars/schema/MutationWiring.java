package com.networknt.starwars.schema;

import graphql.schema.DataFetcher;

/**
 * Created by Nicholas Azar on October 16, 2017.
 */
public class MutationWiring {

    public static class Context {
        final NumberHolder numberHolder;

        public Context(int theNumber) {
            this.numberHolder = new NumberHolder(theNumber);
        }

        public NumberHolder getNumberHolder() {
            return this.numberHolder;
        }
    }

    private static Context context = new Context(5);

    static DataFetcher numberHolderFetcher = dataFetchingEnvironment -> context.getNumberHolder();

    static DataFetcher theNumberFetcher = dataFetchingEnvironment -> context.getNumberHolder().getTheNumber();

    static DataFetcher changeTheNumberFetcher = dataFetchingEnvironment -> {
        context.getNumberHolder().setTheNumber(dataFetchingEnvironment.getArgument("newNumber"));
        return context.getNumberHolder();
    };

    static DataFetcher failToChangeTheNumberFetcher = dataFetchingEnvironment -> {
        throw new RuntimeException("Simulate failing to change the number.");
    };

}
