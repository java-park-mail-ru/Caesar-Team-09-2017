package server.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import server.error.Error;

import java.util.List;


@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity createAccount(Account account) {

        try {
            String encryptedPassword = passwordEncoder().encode(account.getPassword());
            final String sql = "INSERT INTO FUser(username, email, password) VALUES(?,?,?)";
            jdbcTemplate.update(sql,
                    new Object[]{ account.getUsername(), account.getEmail(), encryptedPassword });

            return new ResponseEntity(account, HttpStatus.CREATED); // 201

        } catch (DuplicateKeyException e) {

            final String sql = "SELECT * FROM FUser " +
                    "WHERE LOWER(username COLLATE \"POSIX\") =  LOWER(? COLLATE \"POSIX\") " +
                    "OR LOWER(email COLLATE \"POSIX\") =  LOWER(? COLLATE \"POSIX\")";

            List<Account> accounts = jdbcTemplate.query(sql,
                    new Object[] { account.getUsername(), account.getEmail() }, new AccountRowMapper());

            return new ResponseEntity(accounts, HttpStatus.CONFLICT); // 409
        }
    }

    public ResponseEntity getAccount(String username) {

        ResponseEntity responseEntity = findAccount(username, jdbcTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        return  responseEntity;
    }

    public ResponseEntity renameAccount(Account account, String username) {

        try {

//          **************************************find account**************************************
            ResponseEntity responseEntity = findAccount(username, jdbcTemplate);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                return responseEntity;
            }
            Account oldAccount = (Account) responseEntity.getBody();
//          **************************************find account**************************************

            if (account.getEmail() == null) {
                account.setEmail(oldAccount.getEmail());
            }

            if (account.getUsername() == null) {
                account.setUsername(oldAccount.getUsername());
            }


            final String sql = "UPDATE FUser SET email = ?, username = ? " +
                    "WHERE LOWER(username COLLATE \"POSIX\") =  LOWER(? COLLATE \"POSIX\")";
            jdbcTemplate.update(sql, account.getEmail(), account.getUsername(), username);

            return new ResponseEntity(account, HttpStatus.OK);

        } catch (DuplicateKeyException e) {

            return new ResponseEntity(Error.getJson("this username/email has already existed"), HttpStatus.CONFLICT); // 409

        }
    }

    public static ResponseEntity findAccount(String username, JdbcTemplate jdbcTemplate) {
        try {

            final String sql = "SELECT * from FUser " +
                    "WHERE LOWER(username COLLATE \"POSIX\") = LOWER(? COLLATE \"POSIX\")";
            Account account = (Account) jdbcTemplate.queryForObject(
                    sql, new Object[]{ username }, new AccountRowMapper());

            return new ResponseEntity(account, HttpStatus.OK);

        } catch (EmptyResultDataAccessException e) {

            return new ResponseEntity(Error.getJson("Can't find user with username: " + username),
                    HttpStatus.NOT_FOUND);
        }
    }

    public  boolean checkPassword(String username, String password) {
        final String sql = "SELECT password from FUser " +
                "WHERE LOWER(username COLLATE \"POSIX\") = LOWER(? COLLATE \"POSIX\")";
        String encryptedPassword = (String) jdbcTemplate.queryForObject(
                sql, new Object[]{ username }, String.class);

        return passwordEncoder().matches(password, encryptedPassword);
    }

    public ResponseEntity getAccountsScore() {

        final String sql = ("SELECT * FROM FUser order by score");

        List<Account> accounts = jdbcTemplate.query(sql.toString(), new AccountRowMapper());

        return new ResponseEntity(accounts, HttpStatus.OK);
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

