package com.networknt.eventuate.queryservice.account;



import com.networknt.eventuate.account.common.model.account.AccountHistoryEntry;
import com.networknt.eventuate.account.common.model.account.AccountTransactionInfo;
import com.networknt.eventuate.common.Int128;
import com.networknt.service.SingletonServiceFactory;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Junit test class for UserRepositoryImpl.
 * use H2 test database for data source
 */
public class AccountInfoRepositoryImplTest {

    public static DataSource ds;

    static {
        ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);
       try (Connection connection = ds.getConnection()) {
            // Runscript doesn't work need to execute batch here.
            String schemaResourceName = "/queryside_ddl.sql";
            InputStream in = AccountInfoRepositoryImplTest.class.getResourceAsStream(schemaResourceName);

            if (in == null) {
                throw new RuntimeException("Failed to load resource: " + schemaResourceName);
            }
            InputStreamReader reader = new InputStreamReader(in);
            RunScript.execute(connection, reader);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AccountInfoRepository accountRepository = (AccountInfoRepository) SingletonServiceFactory.getBean(AccountInfoRepository.class);


    private static AccountInfo account;
    private static AccountTransactionInfo ti;
    private static  String  id;
    @BeforeClass
    public static void setUp() {
        Int128 idGen = new Int128(1222L, 1011L);
        id = idGen.asString();
        account = new AccountInfo();
        account.setId(id);
        account.setCustomerId("2233456");
        account.setTitle("Banking account");
        account.setDescription("test account");
        account.setBalance(20000000L);
        account.setVersion("1");

        ti = new AccountTransactionInfo();
        ti.setTransactionId(id);
        ti.setFromAccountId(id);
        ti.setToAccountId(id);
        ti.setAmount(100000L);
        ti.setDescription("test");
        ti.setEntryType(AccountHistoryEntry.EntryType.transaction);
    }

    @Test
    public void testSave() {
        int result = accountRepository.createAccount(account);
        assertTrue(result>0);
    }

    @Test
    public void testFindOneAccount() {
        AccountInfo result = accountRepository.findOneAccount(id);
        assertNotNull(result);
    }

    @Test
    public void testFindByEmail() {
        List<AccountInfo> result = accountRepository.findByCustomerId("2233456");
        assertTrue(result.size()>0);
     }

    @Test
    public void testAddTransaction() {
        int result = accountRepository.addTransaction(ti);
        assertTrue(result>0);
    }

 /*   @Test
    public void testGetTransactions() {
        System.out.println(id);
        List<AccountTransactionInfo> result = accountRepository.getAccountTransactionHistory(id);
        assertTrue(result.size()>0);
    }*/
}
