package server.account;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import server.account.dao.AccountDao;
import server.account.dao.AccountDaoImpl;


import java.util.List;

@Service
public class AccountService {

    private final JdbcTemplate jdbcTemplate;
    private  AccountDaoImpl accountDaoimpl;
    private final PasswordEncoder passwordEncoder;


    public AccountService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, AccountDaoImpl accountDaoimpl) {
            this.jdbcTemplate = jdbcTemplate;
            this.passwordEncoder = passwordEncoder;
            this.accountDaoimpl = accountDaoimpl;
    }

    public AccountDao createAccount(Account account) {

        String encryptedPassword = passwordEncoder.encode(account.getPassword());

        AccountDao accountDao = new AccountDao(account.getEmail(), account.getUsername(), encryptedPassword);
        accountDaoimpl.insertAccount(accountDao);

        return accountDao;
    }

    public AccountDao getAccount(String username) {

        AccountDao accountDao = accountDaoimpl.getAccount(username);

        return  accountDao;
    }

    public AccountDao renameAccount(Account account, String username) {

        AccountDao accountDao = new AccountDao(account);
        accountDaoimpl.renameAccount(accountDao, username);

        return accountDao;
    }

    public  boolean checkPassword(String username, String password) {
        final String sql = "SELECT password from FUser "
                + "WHERE LOWER(username COLLATE \"POSIX\") = LOWER(? COLLATE \"POSIX\")";
        String encryptedPassword = (String) jdbcTemplate.queryForObject(
                sql, new Object[]{username}, String.class);

        return passwordEncoder.matches(password, encryptedPassword);
    }

    public List<AccountDao> getAccountsScore() {

        List<AccountDao> accountsDao = accountDaoimpl.getScoreAccount();

        return accountsDao;
    }
}

