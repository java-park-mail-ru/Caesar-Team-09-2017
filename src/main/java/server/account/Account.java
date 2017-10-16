package server.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.validation.constraints.NotNull;


public class Account {

    private String email;
    private String username;
    private String password;
    private long score;

    @JsonCreator
    public Account(@JsonProperty(value = "email", required = true) String email,
                   @JsonProperty(value = "username", required = true) String username,
                   @JsonProperty(value = "password", required = true) String password) {

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

    @JsonIgnore
    public ObjectNode getJson(){
        final ObjectMapper map = new ObjectMapper();
        final ObjectNode node = map.createObjectNode();

        node.put("email", this.email);
        node.put("username", this.username);
        node.put("password", this.password);
        node.put("score", this.score);

        return node;
    }
}
