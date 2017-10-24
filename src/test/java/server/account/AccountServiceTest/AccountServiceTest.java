package server.account.AccountServiceTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import server.account.Account;
import server.account.AccountService;
import server.account.dao.AccountDao;

import java.security.SecureRandom;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static service.ServiceDb.clearDatabase;
import static utils.TestUtils.getRandomString;

@SpringBootTest()
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void clearDataBaseBefore() throws SQLException {
        clearDatabase(jdbcTemplate);
    }

    @After
    public void clearDatabaseAfter() throws SQLException {
        clearDatabase(jdbcTemplate);
    }

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Before
    public void setUp() {
        accountService.createAccount(new Account("seva@mail.ru", "seva", "qwerty"));
    }

    @Test
    public void createSimpleAccount() {
        accountService.createAccount(new Account(getRandomString(SECURE_RANDOM, 10) + "@mail.ru",
                getRandomString(SECURE_RANDOM, 10), getRandomString(SECURE_RANDOM, 10)));
        accountService.createAccount(new Account(getRandomString(SECURE_RANDOM, 10) + "@mail.ru",
                getRandomString(SECURE_RANDOM, 10), getRandomString(SECURE_RANDOM, 10)));
        accountService.createAccount(new Account(getRandomString(SECURE_RANDOM, 10) + "@mail.ru",
                getRandomString(SECURE_RANDOM, 10), getRandomString(SECURE_RANDOM, 10)));
    }

    @Test
    public void createExistUsernameOrEmailAccount() {

        String status = accountService.createAccount(
                new Account("seva@mail.ru",
                getRandomString(SECURE_RANDOM, 10),
                getRandomString(SECURE_RANDOM, 10))).getStatus();
        assertEquals("CONFLICT", status);

        status = accountService.createAccount(
                new Account(getRandomString(SECURE_RANDOM, 10) + "@mail.ru",
                        "seva", getRandomString(SECURE_RANDOM, 10))).getStatus();
        assertEquals("CONFLICT", status);
    }

    @Test
    public void getAccount() {
        AccountDao accountDao = accountService.getAccount("seva");
        assertEquals("OK", accountDao.getStatus());
        assertEquals("seva", accountDao.getUsername());
        assertEquals("seva@mail.ru", accountDao.getEmail());
    }

    @Test
    public void getAccountNotExist() throws ClassCastException {
        assertEquals("NOT_FOUND", accountService.getAccount("username5").getStatus());
    }

    @Test
    public void checkPasswordTrue() throws ClassCastException {
        assertTrue(accountService.checkPassword("seva", "qwerty"));
    }

    @Test
    public void checkPasswordFalse() throws ClassCastException {
        assertFalse(accountService.checkPassword("seva", "qwerty123"));
    }

    @Test
    public void renameSimpleAccount() {
        String username = getRandomString(SECURE_RANDOM, 10);
        String password = getRandomString(SECURE_RANDOM, 10);
        String email = getRandomString(SECURE_RANDOM, 10) + "@mail.ru";
        accountService.createAccount(new Account(email, username, password));
        String newUsername = getRandomString(SECURE_RANDOM, 10);
        String newEmail = getRandomString(SECURE_RANDOM, 10) + "@mail.ru";
        AccountDao accountDao = accountService.renameAccount(
                new Account(newEmail, newUsername, password), username);
        assertEquals(newEmail, accountDao.getEmail());
        assertEquals(newUsername, accountDao.getUsername());

    }

    @Test
    public void renameAccountDoesNotExist() {
        String username = getRandomString(SECURE_RANDOM, 10);
        String password = getRandomString(SECURE_RANDOM, 10);
        String email = getRandomString(SECURE_RANDOM, 10) + "@mail.ru";
        String newUsername = getRandomString(SECURE_RANDOM, 10);
        assertEquals("NOT_FOUND", accountService.renameAccount(
                new Account(email, username, password), newUsername).getStatus());

    }

    @Test
    public void renameAccountToExistAccount() {
        String username1 = getRandomString(SECURE_RANDOM, 10);
        String password1 = getRandomString(SECURE_RANDOM, 10);
        String email1 = getRandomString(SECURE_RANDOM, 10) + "@mail.ru";
        accountService.createAccount(new Account(email1, username1, password1));

        String username2 = getRandomString(SECURE_RANDOM, 10);
        String password2 = getRandomString(SECURE_RANDOM, 10);
        String email2 = getRandomString(SECURE_RANDOM, 10) + "@mail.ru";
        accountService.createAccount(new Account(email2, username2, password2));

        String newUsername = getRandomString(SECURE_RANDOM, 10);
        assertEquals("CONFLICT", accountService.renameAccount(
                new Account(email1, newUsername, password2), username2).getStatus());
    }

}
