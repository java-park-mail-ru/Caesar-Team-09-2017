package technopark.controllers;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotNull;

import static org.junit.Assert.assertEquals;
import static utils.TestUtils.createJsonResponse;
import static technopark.services.ServiceDb.clearDatabase;

import java.sql.SQLException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WebApplicationContext wac; // for mockSession

    protected MockHttpSession mockSession; // for test double login

    @After
    public void clearDatabaseAfter() throws SQLException {
        clearDatabase(jdbcTemplate);
    }

    @Before
    public void setup() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,"seva", "seva@mail.ru", "qwerty");
        signupWithoutChecks(json);

        mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    }

    @Test
    public void simpleRegister() throws Exception {
        clearDatabase(jdbcTemplate);

        final JSONObject json = new JSONObject();
        createJsonResponse(json,null, null, null);
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
        String[] username = { "Vera", "Varya"};
        String[] email = { "Vera@mail.ru", "Varya@mail.ru"};
        // first account
        final JSONObject json = new JSONObject();
        createJsonResponse(json, username[0], email[0], null);
        signupWithoutChecks(json);
        // second account
        createJsonResponse(json, username[1], email[1], null);
        signupWithoutChecks(json);
        // try to signup with first username
        createJsonResponse(json, username[0], null, null);
        MvcResult result = signupWithoutChecks(json);
        assertEquals(409, result.getResponse().getStatus());
        // try to signup with second email
        createJsonResponse(json, null, email[1], null);
        result = signupWithoutChecks(json);
        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    public void emptyCredentials() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,null, null, null);
        json.remove("email");
        MvcResult result = signupWithoutChecks(json);
        assertEquals(201, result.getResponse().getStatus());

        createJsonResponse(json,null, null, null);
        json.remove("username");
        result = signupWithoutChecks(json);
        assertEquals(400, result.getResponse().getStatus());

        createJsonResponse(json,null, null, null);
        json.remove("password");
        result = signupWithoutChecks(json);
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void authorizedAfterRegister() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,null, null, null);
        mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isCreated())
                .andExpect(request().sessionAttribute("username", json.getString("username")));
    }

    @Test
    public void simplyLogIn() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,"seva", "seva@mail.ru", "qwerty");
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
    public void accountDoesNotExistLogIn() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,null, null, null);
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
        createJsonResponse(json,"seva", "seva@mail.ru", "NOT QWERTY");
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
        createJsonResponse(json,"seva", "seva@mail.ru", "qwerty");
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

    @Test
    public void checkAuthorizedTrue() throws Exception {
        mockMvc
                .perform(get("/api/auth/me")
                        .sessionAttr("username", "seva"))
                .andExpect(status().isOk());
    }

    @Test
	public void checkAuthorizedFalse() throws Exception {
		mockMvc
				.perform(get("/api/auth/me"))
				.andExpect(status().isUnauthorized());
	}

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

    @Test
    public void simpleRename() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,null, null, null);
        mockMvc
                .perform(post("/api/auth/signup")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()));

        createJsonResponse(json,null, null, null);
        mockMvc
                .perform(post("/api/user/rename")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(json.get("username")))
                .andExpect(jsonPath("email").value(json.get("email")));
    }

    @Test
    public void notAuthorizedRename() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,"seva", "seva@mail.ru", "qwerty");
        mockMvc
                .perform(post("/api/user/rename")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void checkAuthorizedAfterRename() throws Exception {
        final JSONObject json = new JSONObject();
        createJsonResponse(json,null, null, null);
        mockMvc
                .perform(post("/api/auth/signup")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()));

        createJsonResponse(json,null, null, null);
        mockMvc
                .perform(post("/api/user/rename")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("username", json.getString("username")))
                .andExpect(jsonPath("username").value(json.get("username")))
                .andExpect(jsonPath("email").value(json.get("email")));
    }

    private MvcResult signupWithoutChecks(@NotNull JSONObject json) throws Exception {
        return mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json.toString()))
                        .andReturn();
    }
}
