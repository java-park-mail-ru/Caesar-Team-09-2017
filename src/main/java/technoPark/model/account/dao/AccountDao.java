package technoPark.model.account.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import technoPark.model.account.Account;
import technoPark.model.id.Id;

public class AccountDao {

    private String email;
    private String username;
    private String password;
    private long score;
    private long id;

    private String status;

    private String error;

    @JsonCreator
    public AccountDao(String email, String username, String password) {

        this.email = email;
        this.username = username;
        this.password = password;
    }
    public AccountDao() {

    }

    public AccountDao(Account account) {
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.password = account.getPassword();
    }

    @JsonIgnore
    public String getError() {
        return error;
    }

    public Id<AccountDao> getId() {
        return new Id<AccountDao>(id);
    }

    public long getIdLong() {
        return id;
    }

    public long getScore() {
        return score;
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }


    public void setError(String error) {
        this.error = error;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
