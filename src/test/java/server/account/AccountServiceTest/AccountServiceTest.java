package server.account.AccountServiceTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import server.account.Account;
import server.account.AccountService;

import java.security.SecureRandom;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static service.ServiceService.clearDatabase;

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

        HttpStatus httpStatus = accountService.createAccount(
                new Account("seva@mail.ru",
                getRandomString(SECURE_RANDOM, 10),
                getRandomString(SECURE_RANDOM, 10))).getStatusCode();
        assertEquals(HttpStatus.CONFLICT, httpStatus);

        httpStatus = accountService.createAccount(
                new Account(getRandomString(SECURE_RANDOM, 10) + "@mail.ru",
                        "seva", getRandomString(SECURE_RANDOM, 10))).getStatusCode();
        assertEquals(HttpStatus.CONFLICT, httpStatus);
    }

    @Test
    public void getAccount() {
        ResponseEntity responseEntity = accountService.getAccount("seva");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final Account accountByUsername = (Account) responseEntity.getBody();
        assertEquals("seva", accountByUsername.getUsername());
        assertEquals("seva@mail.ru", accountByUsername.getEmail());
    }

    @Test
    public void getAccountNotExist() throws ClassCastException {
        assertEquals(HttpStatus.NOT_FOUND, accountService.getAccount("username5").getStatusCode());
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
        Account account = (Account) accountService.renameAccount(
                new Account(newEmail, newUsername, password), username).getBody();
        assertEquals(newEmail, account.getEmail());
        assertEquals(newUsername, account.getUsername());

    }

    public String getRandomString(SecureRandom random, int length){
        final String lettersAndDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*";
        final StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; ++i){
            stringBuilder.append(lettersAndDigits.toCharArray()[random.nextInt(lettersAndDigits.length())]);
        }
        return stringBuilder.toString();
    }

}
