package server;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {

    private Map<String, User> users = new HashMap<>();

    public void setUser(String username, User user) {
        String encryptedPassword = passwordEncoder().encode(user.getPassword());
        User userWithEncryptedPassword = user.copy();
        userWithEncryptedPassword.setPassword(encryptedPassword);
        users.put(username, userWithEncryptedPassword);
    }

    private boolean containsEmail(String email) {

        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (entry.getValue().getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsUsername(String username) {
        return users.containsKey(username);
    }

    public String getEmail(String username) {
        return users.get(username).getEmail();
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }

    public  boolean checkPassword(String username, String password) {
        String encryptedPassword = users.get(username).getPassword();
        System.out.println(password);
        System.out.println(encryptedPassword);
        return passwordEncoder().matches(password, encryptedPassword);
    }

    public boolean validation(User user, Map<String, String> response) {
        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();

        if (email == null) {
            email = "Incorrect format. Use this format for email: \"email\":\"yourEmail@mail.ru\"";
            response.put("Cause", email);
            return false;
        }

        if (username == null) {
            username = "Incorrect format. Use this format for username: \"username\":\"yourUsername\"";
            response.put("Cause", username);

            return false;
        }

        if (password == null) {
            password = "Incorrect format. Use this format for password: \"password\":\"yourPassword\"";
            response.put("Cause", password);

            return false;
        }

        if (containsEmail(email)) {
            response.put("Cause", "This email \"" + email + "\" exists");

            return false;
        }

        if (containsUsername(username)) {
            response.put("Cause", "This username \"" + username + "\" exists");

            return false;
        }

        return true;
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

