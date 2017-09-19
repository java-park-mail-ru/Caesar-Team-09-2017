package server;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private Map<String, User> users = new HashMap<>();

    public void setUser(String username, User user) {
        users.put(username, user);
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

    public String getPassword(String username) {
        return users.get(username).getPassword();
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

    public boolean validation(String email, String username, String password, Map<String, String> response) {
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
}

