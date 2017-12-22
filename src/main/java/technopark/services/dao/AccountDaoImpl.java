package technopark.services.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import technopark.account.dao.AccountDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class AccountDaoImpl implements AccountDaoInterface {

    private final JdbcTemplate jdbcTemplate;

    public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AccountDao insertAccount(AccountDao accountDao) {
        try {

            final String sql = "INSERT INTO FUser(username, email, password) VALUES(?,?,?)";
            jdbcTemplate.update(sql,
                    new Object[]{accountDao.getUsername(), accountDao.getEmail(), accountDao.getPassword()});

            accountDao.setStatus("CREATED");

        } catch (DuplicateKeyException e) {

            accountDao.setStatus("CONFLICT");

        } finally {
            return accountDao;
        }
    }

    @Override
    public AccountDao getAccount(String username) {
        AccountDao accountDao = new AccountDao();
        try {

            final String sql = "SELECT * from FUser "
                    + "WHERE LOWER(username COLLATE \"POSIX\") = LOWER(? COLLATE \"POSIX\")";
            accountDao = jdbcTemplate.queryForObject(
                    sql, new Object[]{username},  AccountDaoImpl::readItem);

            accountDao.setStatus("OK");


        } catch (EmptyResultDataAccessException e) {

            accountDao.setStatus("NOT_FOUND");
            accountDao.setError("Can't find user with username: " + username);

        } finally {
            return accountDao;
        }
    }

    @Override
    public AccountDao renameAccount(AccountDao accountDao, String username) {

        AccountDao oldAccountDao = getAccount(username);
        if (oldAccountDao.getStatus().equals("NOT_FOUND")) {
            accountDao.setStatus("NOT_FOUND");
            return accountDao;
        }

        if (accountDao.getEmail() == null) {
            accountDao.setEmail(oldAccountDao.getEmail());
        }

        if (accountDao.getUsername() == null) {
            accountDao.setUsername(oldAccountDao.getUsername());
        }

        try {
            final String sql = "UPDATE FUser SET email = ?, username = ? "
                    + "WHERE LOWER(username COLLATE \"POSIX\") =  LOWER(? COLLATE \"POSIX\")";
            jdbcTemplate.update(sql, accountDao.getEmail(), accountDao.getUsername(), username);

            accountDao.setStatus("OK");

        } catch (DuplicateKeyException e) {

            accountDao.setStatus("CONFLICT");
            accountDao.setError("this username/email has already existed");
        } finally {
            return accountDao;
        }
    }

    @Override
    public List<AccountDao> getScoreAccount() {

        final String sql = ("SELECT * FROM FUser order by score");

        List<AccountDao> accountsDao = jdbcTemplate.query(sql.toString(), AccountDaoImpl::readItem);

        return accountsDao;
    }

    @Override
    public AccountDao getAccountId(long id) {

        AccountDao accountDao = new AccountDao();

        try {
            final String sql = ("SELECT * FROM FUser where id = ?");
            accountDao = jdbcTemplate.queryForObject(sql.toString(), new Object[]{id}, AccountDaoImpl::readItem);

            accountDao.setStatus("OK");
        } catch (EmptyResultDataAccessException e) {
            accountDao.setStatus("NOT_FOUND");
        } finally {
            return accountDao;
        }
    }

    @Override
    public void setScore(AccountDao accountDao, int score) {
        try {
            String sqlUpdate = "UPDATE FUser SET score = score + ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdate, score, accountDao.getId().getId());
        } catch (EmptyResultDataAccessException e) {
            accountDao.setStatus("NOT_FOUND");
        }

    }

    private static AccountDao readItem(ResultSet rs, int rowNum) throws SQLException {
        AccountDao accountDao = new AccountDao();

        accountDao.setUsername(rs.getString("username"));
        accountDao.setEmail(rs.getString("email"));
        accountDao.setScore(rs.getLong("score"));
        accountDao.setId(rs.getInt("id"));

        return accountDao;
    }
}
