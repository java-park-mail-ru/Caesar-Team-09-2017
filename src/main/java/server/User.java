package server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User {

    private String email;
    private String username;
    private String password;

    @JsonCreator
    public User(@JsonProperty("email") String email, @JsonProperty("username") String username,
                @JsonProperty("password") String password) {

        this.email = email;
        this.username = username;
        this.password = password;

    }

    public User(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
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

    public void setPassword(String password) {
        this.password = password;
    }

    public User copy() {
        return new User(this);
    }

}
