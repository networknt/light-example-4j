package com.networknt.eventuate.queryservice.account;




import com.networknt.eventuate.account.common.model.account.AccountChangeInfo;
import com.networknt.eventuate.account.common.model.account.AccountTransactionInfo;
import com.networknt.eventuate.account.common.model.transaction.TransferState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AccountInfoRepositoryImpl implements  AccountInfoRepository{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    public AccountInfoRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {this.dataSource = dataSource;}

    public List<AccountInfo> findByCustomerId(String customerId) {
        Objects.requireNonNull(customerId);
        List<AccountInfo> accounts = new ArrayList<AccountInfo>();

        String psSelect = "SELECT a.account_id, c.customer_id,  title, version, description, balance, creation_Date " +
                "FROM account_info a join account_customer c ON a.account_id =c.account_id WHERE ACTIVE_FLG = 'Y' AND c.customer_id = ?";

        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs == null ) {
                logger.error("incorrect fetch result {}", customerId);
            }
            while (rs.next()) {
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setId(rs.getString("account_id"));
                accountInfo.setCustomerId(rs.getString("customer_id"));
                accountInfo.setTitle(rs.getString("title"));
                accountInfo.setDescription(rs.getString("description"));
                accountInfo.setVersion(rs.getString("version"));
                accountInfo.setBalance(MoneyUtil.toLongRepr(rs.getBigDecimal("balance")));
                accountInfo.setCreationDate(rs.getDate("creation_Date"));

                accounts.add(accountInfo);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }


        return accounts;
    }

    public AccountInfo findOneAccount(String accountId){
        Objects.requireNonNull(accountId);
        String psSelect = "SELECT a.account_id, c.customer_id,  title, version, description, balance, creation_Date " +
                        "FROM account_info a join account_customer c ON a.account_id =c.account_id WHERE ACTIVE_FLG = 'Y' AND a.account_id = ?";
        AccountInfo accountInfo = null;
        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs == null || rs.getFetchSize() > 1) {
                logger.error("incorrect fetch result {}", accountId);
            }
            while (rs.next()) {
                accountInfo = new AccountInfo();
                accountInfo.setId(rs.getString("account_id"));
                accountInfo.setCustomerId(rs.getString("customer_id"));
                accountInfo.setTitle(rs.getString("title"));
                accountInfo.setDescription(rs.getString("description"));
                accountInfo.setVersion(rs.getString("version"));
                accountInfo.setBalance(MoneyUtil.toLongRepr(rs.getBigDecimal("balance")));
                accountInfo.setCreationDate(rs.getDate("creation_Date"));


            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return accountInfo;
    }

    public void delete(String accountId){
        Objects.requireNonNull(accountId);
        String psDelete = "UPDATE account_info SET ACTIVE_FLG = 'N' WHERE account_id = ?";
        String psDelete2 = "DELETE FROM account_customer WHERE account_id = ?";
        try (final Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement psEntity = connection.prepareStatement(psDelete);
            psEntity.setString(1, accountId);
            int count = psEntity.executeUpdate();
            if (count != 1) {
                logger.info("Failed to update account_info: {}", accountId);
            } else {
                try (PreparedStatement psXref = connection.prepareStatement(psDelete2)) {
                    psXref.setString(1, accountId);
                    psXref.executeUpdate();
                }

                connection.commit();
            }

        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

    }

    public int createAccount(AccountInfo accountInfo) {
        Objects.requireNonNull(accountInfo);

        String psInsert = "INSERT INTO account_info (account_id, title, description, version, balance,creation_Date) VALUES (?, ? ,?,?,? , current_date)";
        String psInsert2 = "INSERT INTO account_customer (account_id, customer_Id) VALUES (?, ?)";
        int count = 0;
        try (final Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, accountInfo.getId());
            stmt.setString(2, accountInfo.getTitle());
            stmt.setString(3, accountInfo.getDescription());
            stmt.setString(4, accountInfo.getVersion());
            stmt.setLong(5, accountInfo.getBalance());

            count = stmt.executeUpdate();
            if (count>0 && accountInfo.getCustomerId()!=null) {
                try (PreparedStatement psXref = connection.prepareStatement(psInsert2)) {

                    psXref.setString(1,  accountInfo.getId());
                    psXref.setString(2, accountInfo.getCustomerId());

                    psXref.executeUpdate();
                }
            }
            connection.commit();

            if (count != 1) {
                logger.error("Failed to insert account_info: {}", accountInfo.getId());
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return count;
    }

    public int addTransaction(AccountTransactionInfo ti){
        Objects.requireNonNull(ti);
        String psInsert = "INSERT INTO account_transaction (transaction_Id,from_account_id, to_account_id, amount, description, status, entry_Type,creation_Date) VALUES (?, ? ,?,?,?,?,?, current_date)";

        int count = 0;
        try (final Connection connection = dataSource.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, ti.getTransactionId());
            stmt.setString(2, ti.getFromAccountId());
            stmt.setString(3, ti.getToAccountId());
            stmt.setLong(4, ti.getAmount());
            stmt.setString(5, ti.getDescription());
            stmt.setString(6, ti.getStatus().name());
            stmt.setString(7, ti.getEntryType().name());

            count = stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return count;
    }

    public int updateTransactionStatus(String transactionId, TransferState status){
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(status);
        int count = 0;
        String psInsert = "UPDATE account_transaction SET status = ? WHERE  transaction_Id = ?";
        try (final Connection connection = dataSource.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(psInsert);

            stmt.setString(1, status.name());
            stmt.setString(2, transactionId);

            count = stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }


        return count;
    }

    public int updateBalance(String accountId, String changeId, long balanceDelta, AccountChangeInfo ci){
        int count = 0;
        String psUpdate = "UPDATE account_info SET balance = balance + ? WHERE  account_id = ?";
        String psInsert = "INSERT INTO account_change_info (change_id,transaction_id, transaction_type, amount, balanceDelta,creation_Date) VALUES (?,?,?,?,?, current_date)";

        try (final Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(psUpdate);
            stmt.setLong(1, balanceDelta);
            stmt.setString(2, accountId);

            count = stmt.executeUpdate();
            if (count>0) {
                try (PreparedStatement psXref = connection.prepareStatement(psInsert)) {

                    psXref.setString(1,  changeId);
                    psXref.setString(2, ci.getTransactionId());
                    psXref.setString(3, ci.getTransactionType());
                    psXref.setLong(4, ci.getAmount());
                    psXref.setLong(5, ci.getAmount());

                    psXref.executeUpdate();
                }
            }
            connection.commit();

            if (count != 1) {
                logger.error("Failed to insert account_info: {}", accountId);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return count;
    }

    public List<AccountTransactionInfo> getAccountTransactionHistory(String accountId) {
        List<AccountTransactionInfo> transactions = new ArrayList<AccountTransactionInfo>();
        String psSelect ="select transaction_Id,from_account_id, to_account_id, amount, description, status, entry_Type,creation_Date from account_transaction where from_account_id = ? or to_account_id =?";


        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, accountId);
            stmt.setString(2, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs == null ) {
                logger.error("incorrect fetch result {}", accountId);
            }

            while (rs.next()) {
                AccountTransactionInfo transaction = new AccountTransactionInfo();
                transaction.setTransactionId(rs.getString("transaction_Id"));
                transaction.setFromAccountId(rs.getString("from_account_id"));
                transaction.setToAccountId(rs.getString("to_account_id"));
                transaction.setDescription(rs.getString("description"));
                transaction.setStatus(TransferState.valueOf(rs.getString("status")));
                transaction.setAmount(MoneyUtil.toLongRepr(rs.getBigDecimal("amount")));
                transaction.setDate(rs.getDate("creation_Date"));

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }


        return transactions;
    }
}
