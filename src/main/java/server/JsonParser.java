package server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonParser {

    private String email;
    private String username;
    private String password;

    @JsonCreator
    JsonParser(@JsonProperty("email") String email, @JsonProperty("username") String username,
               @JsonProperty("password") String password) {

        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
