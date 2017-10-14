package server.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Account {

    private String email;
    private String username;
    private String password;
    private long score;

    @JsonCreator
    public Account(@JsonProperty("email") String email, @JsonProperty("username") String username,
                   @JsonProperty("password") String password) {

        this.email = email;
        this.username = username;
        this.password = password;

    }

    public Account(){

    }

    public Account(Account account) {
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.password = account.getPassword();
    }

    public Account copy() {
        return new Account(this);
    }


    public long getScore() {
        return score;
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

    public void setScore(long score) {
        this.score = score;
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
