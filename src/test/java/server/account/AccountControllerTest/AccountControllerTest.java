package server.account.AccountControllerTest;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import server.account.Account;
import server.account.AccountService;

import static service.ServiceService.clearDatabase;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
//@Transactional
public class AccountControllerTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private WebApplicationContext wac; // for mockSession

    protected MockHttpSession mockSession; // for test double login

    @Before
    public void clearDataBaseBefore() throws SQLException {
        clearDatabase(jdbcTemplate);
    }

    @After
    public void clearDatabaseAfter() throws SQLException {
        clearDatabase(jdbcTemplate);
    }

    @Before
    public void setup() {
        accountService.createAccount(new Account("seva@mail.ru","seva", "qwerty"));
        mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    }

// *************************************** CREATE ACCOUNT START***************************************
    @Test
    public void simpleRegister() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", getRandomString(SECURE_RANDOM, 10));
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        json.put("email", getRandomString(SECURE_RANDOM, 10) + "@mail.ru");

        mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value(json.get("username")))
                .andExpect(jsonPath("email").value(json.get("email")));
    }

    @SuppressWarnings("")
    @Test
    public void usernameOrEmailAlreadyExists() throws Exception {
        // signup two account
        String[] username = { getRandomString(SECURE_RANDOM, 10), getRandomString(SECURE_RANDOM, 10)};
        String[] email = { getRandomString(SECURE_RANDOM, 10) + "@mail.ru", getRandomString(SECURE_RANDOM, 10) + "@mail.ru"};
        // first account
        final JSONObject json = new JSONObject();
        json.put("username", username[0]);
        json.put("email", email[0]);
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isCreated());
        // second account
        json.put("username", username[1]);
        json.put("email", email[1]);
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isCreated());
        // try to signup with first username
        json.put("username", username[0]);
        json.put("email", getRandomString(SECURE_RANDOM, 10) + "@mail.ru");
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$[0].email").value(email[0]))
                .andExpect(jsonPath("$[0].username").value(username[0]));
        // try to signup with second email
        json.put("username", getRandomString(SECURE_RANDOM, 10) + "@mail.ru");
        json.put("email", email[1]);
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$[0].email").value(email[1]))
                .andExpect(jsonPath("$[0].username").value(username[1]));
        // try to signup with first email and second username
        json.put("username", username[0]);
        json.put("email", email[1]);
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$[0].email").value(email[0]))
                .andExpect(jsonPath("$[0].username").value(username[0]))
                .andExpect(jsonPath("$[1].email").value(email[1]))
                .andExpect(jsonPath("$[1].username").value(username[1]));
    }

    @Test
    public void emptyCredentials() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", getRandomString(SECURE_RANDOM, 10));
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
        json.remove("username");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
        json.remove("password");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void authorizedAfterRegister() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", getRandomString(SECURE_RANDOM, 10));
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        json.put("email", getRandomString(SECURE_RANDOM, 10) + "@mail.ru");
        mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isCreated())
                .andExpect(request().sessionAttribute("username", json.getString("username")));
    }
// *************************************** CREATE ACCOUNT END***************************************

// *************************************** LOGIN START***************************************
    @Test
    public void simplyLogIn() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", "seva");
        json.put("password", "qwerty");
        json.put("email", "seva@mail.ru");
        mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("username", json.getString("username")))
                .andExpect(jsonPath("username").value(json.get("username")))
                .andExpect(jsonPath("email").value(json.get("email")));
    }

    @Test
    public void AccountDoesNotExistLogIn() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", getRandomString(SECURE_RANDOM, 10));
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        json.put("email", getRandomString(SECURE_RANDOM, 10) + "@mail.ru");
        mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Can't find user with username: " + json.getString("username")));
    }

    @Test
    public void wrongPasswordLogIn() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", "seva");
        json.put("password", getRandomString(SECURE_RANDOM, 10));
        json.put("email", "seva@mail.ru");
        mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value("Wrong password! Check CapsLock :) and try again."));
    }

    @Test
    public void doubleLogIn() throws Exception {
        final JSONObject json = new JSONObject();
        json.put("username", "seva");
        json.put("password", "qwerty");
        json.put("email", "seva@mail.ru");
        mockMvc
                .perform(post("/api/auth/login")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("username", json.getString("username")))
                .andExpect(jsonPath("username").value(json.get("username")))
                .andExpect(jsonPath("email").value(json.get("email")));

        mockMvc
                .perform(post("/api/auth/login")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isIAmATeapot())
                .andExpect(request().sessionAttribute("username", json.getString("username")))
                .andExpect(jsonPath("message").value("You have already authorized"));

    }
// *************************************** LOGIN END***************************************


// *************************************** ME START***************************************
    @Test
    public void CheckAuthorizedTrue() throws Exception {
        mockMvc
                .perform(get("/api/auth/me")
                        .sessionAttr("username", "seva"))
                .andExpect(status().isOk());
    }

    @Test
	public void CheckAuthorizedFalse() throws Exception {
		mockMvc
				.perform(get("/api/auth/me"))
				.andExpect(status().isUnauthorized());
	}
// *************************************** ME END***************************************



// *************************************** LOGOUT START***************************************
    @Test
    public void logOutTrue() throws Exception {
        mockMvc
                .perform(get("/api/auth/logout")
                        .sessionAttr("username", "seva"))
                .andExpect(status().isOk());
    }

    @Test
    public void logOutFalse() throws Exception {
        mockMvc
                .perform(get("/api/auth/logout"))
                .andExpect(status().isUnauthorized());
    }
// *************************************** LOGOUT END***************************************

	public String getRandomString(SecureRandom random, int length){
		final String lettersAndDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*";
		final StringBuilder stringBuilder = new StringBuilder(length);
		for(int i = 0; i < length; ++i){
			stringBuilder.append(lettersAndDigits.toCharArray()[random.nextInt(lettersAndDigits.length())]);
		}
		return stringBuilder.toString();
	}
}