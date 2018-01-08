package com.networknt.example.sagas.ordersandcustomers.customer.domain;


import java.util.Map;

public interface CustomerRepository {


    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity that is saved
     * @param <S> the generic type
     * @return S the saved entity
     */
    <S extends Customer> S save(S entity);


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    Customer findOne(Long id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return true if an entity with the given id exists, {@literal false} otherwise
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    boolean exists(Long id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    Map<Long, Customer> findAll();



    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    long count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void delete(Long id);



    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();
}
