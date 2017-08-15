package com.networknt.eventuate.queryservice.customer;

import com.networknt.eventuate.account.common.model.customer.Address;
import com.networknt.eventuate.account.common.model.customer.Name;
import com.networknt.eventuate.account.common.model.customer.ToAccountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerViewRepositoryImpl implements CustomerViewRepository {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    public CustomerViewRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {this.dataSource = dataSource;}


    public List<QuerySideCustomer> findByEmailLike(String email){

        Objects.requireNonNull(email);
        String psSelect = "SELECT c.customer_Id,first_name, last_name, email, password, ssn, phoneNumber, street1, street2, city, state, country,  zipCode " +
                "FROM customer c join address a ON a.customer_Id =c.customer_Id " +
                " WHERE ACTIVE_FLG = 'Y' AND email LIKE ?";
        List<QuerySideCustomer> customers = new ArrayList<QuerySideCustomer>();
        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, "%" + email + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuerySideCustomer customer = new QuerySideCustomer();
                customer.setId(rs.getString("customer_Id"));
                Name name= new Name(rs.getString("first_name"), rs.getString("last_name"));
                customer.setName(name);
                customer.setEmail(rs.getString("email"));
                customer.setPassword(rs.getString("password"));
                customer.setSsn(rs.getString("ssn"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                Address address = new Address(rs.getString("street1"), rs.getString("street2"), rs.getString("city"),
                        rs.getString("state"), rs.getString("zipCode"),rs.getString("country"));
                customer.setAddress(address);
                customers.add(customer);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }


        return customers;
    }


    public QuerySideCustomer findOneCustomer(String customerId){
        Objects.requireNonNull(customerId);
        String psSelect = "SELECT c.customer_Id,first_name, last_name, email, password, ssn, phoneNumber, street1, street2, city, state, country, zipCode " +
                "FROM customer c join address a ON a.customer_Id =c.customer_Id " +
                " WHERE ACTIVE_FLG = 'Y' AND c.customer_id = ?";
        QuerySideCustomer customer = null;
        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs == null || rs.getFetchSize() > 1) {
                logger.error("incorrect fetch result {}", customerId);
            }
            while (rs.next()) {
                customer = new QuerySideCustomer();
                customer.setId(rs.getString("customer_Id"));
                Name name= new Name(rs.getString("first_name"), rs.getString("last_name"));
                customer.setName(name);
                customer.setEmail(rs.getString("email"));
                customer.setPassword(rs.getString("password"));
                customer.setSsn(rs.getString("ssn"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                Address address = new Address(rs.getString("street1"), rs.getString("street2"), rs.getString("city"),
                        rs.getString("state"), rs.getString("zipCode"), rs.getString("country"));
                customer.setAddress(address);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return customer;
    }

    public void save(QuerySideCustomer customer){
        Objects.requireNonNull(customer);

        String psInsert = "INSERT INTO customer (customer_Id, first_name, last_name, email, password, ssn,  phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
         String psInsert_address = "INSERT INTO address (customer_Id, street1, street2, state, city, country,  zipcode) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (final Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getName()!=null?customer.getName().getFirstName():null);
            stmt.setString(3, customer.getName()!=null?customer.getName().getLastName():null);
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPassword());
            stmt.setString(6, customer.getSsn());
            stmt.setString(7, customer.getPhoneNumber());
            int count = stmt.executeUpdate();
            if (customer.getAddress() != null) {
                try (PreparedStatement psAddress = connection.prepareStatement(psInsert_address)) {
                    Address address = customer.getAddress();
                    psAddress.setString(1,  customer.getId());
                    psAddress.setString(2, address.getStreet1());
                    psAddress.setString(3, address.getStreet2() );
                    psAddress.setString(4, address.getState());
                    psAddress.setString(5, address.getCity());
                    psAddress.setString(6,  address.getCountry());
                    psAddress.setString(7,  address.getZipCode());

                    psAddress.executeUpdate();
                }
            }
            connection.commit();

            if (count != 1) {
                logger.error("Failed to insert USER_DETAIL: {}", customer.getId());
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

    }

    public int addToAccount(String id, ToAccountInfo accountInfo) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(accountInfo);

        String psInsert = "INSERT INTO account_customer (account_id, customer_Id) VALUES (?, ?)";
        int count = 0;


        try (final Connection connection = dataSource.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, accountInfo.getId());
            stmt.setString(2, id);

            count = stmt.executeUpdate();

            if (count != 1) {
                logger.error("Failed to insert account_customer: {}", accountInfo.getId());
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return count;

    }

    public void deleteToAccount(String id, String accountId) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(accountId);

        String psInsert = "DELETE FROM account_customer WHERE account_id =  AND customer_id = ?";

        try (final Connection connection = dataSource.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1,accountId);
            stmt.setString(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

    }

    public void deleteToAccountFromAllCustomers(String accountId) {
        Objects.requireNonNull(accountId);
        String psInsert = "DELETE FROM account_customer WHERE account_id = ?";

        try (final Connection connection = dataSource.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, accountId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }
    }
}
