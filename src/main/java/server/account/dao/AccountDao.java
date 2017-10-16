package server.account.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import server.account.Account;

public class AccountDao {

    private String email;
    private String username;
    private String password;
    private long score;

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

    @SuppressWarnings("unused")
    @JsonIgnore
    public ObjectNode getJson() {
        final ObjectMapper map = new ObjectMapper();
        final ObjectNode node = map.createObjectNode();

        node.put("email", this.email);
        node.put("username", this.username);
        node.put("password", this.password);
        node.put("score", this.score);

        return node;
    }
}
