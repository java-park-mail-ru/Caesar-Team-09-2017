package server.account;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import server.account.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class AccountServiceTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MockMvc mockMvc;


	@Test
	public void testMeRequiresLogin() throws Exception {
		mockMvc
				.perform(get("/api/auth/me"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testMe() throws Exception {
		accountService.createAccount(new Account("test@test.com", "foo", "bar"));
		mockMvc
				.perform(get("/api/auth/me").sessionAttr("username", "foo"))
				.andExpect(status().isOk());
	}

}