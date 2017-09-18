package server;

import java.util.HashMap;
import java.util.Map;

public class UsersProfile {

    private int sessionId = 0;

    private Map<String, String> users = new HashMap<>();

    private Map<String, String>  usersMail = new HashMap<>();
    private Map<String, String>  mailUsers = new HashMap<>();

    @SuppressWarnings("unused")
    public int getSessionId() {
        return sessionId;
    }

    @SuppressWarnings("unused")
    public void incrementSessionId() {
        sessionId++;
    }

    @SuppressWarnings("unused")
    public String getUsername(String email) {
        return mailUsers.get(email);
    }

    @SuppressWarnings("unused")
    public String getEmail(String username) {
        return usersMail.get(username);
    }

    @SuppressWarnings("unused")
    public String getPassword(String username) {
        return users.get(username);
    }

    @SuppressWarnings("unused")
    public void setUsers(String username, String password) {

        users.put(username,password);
    }

    @SuppressWarnings("unused")
    public void setUsersMail(String username, String email) {
        usersMail.put(username,email);
    }

    @SuppressWarnings("unused")
    public void setMailUsername(String username, String email) {
        mailUsers.put(email,username);
    }

    @SuppressWarnings("unused")
    public boolean containsKeyEmail(String email) {
        return usersMail.containsKey(email);
    }

    @SuppressWarnings("unused")
    public boolean containsKeyUsername(String username) {
        return users.containsKey(username);
    }

    @SuppressWarnings("unused")
    public void removeUser(String username) {
        String email = mailUsers.get(username);
        usersMail.remove(email);
        mailUsers.remove(username);
        users.remove(username);
    }

    @SuppressWarnings("unused")
    public void getAllUsers(Map<String, String> response) {

        users.forEach((key, value) -> {
            response.put(usersMail.get(key), "email");
            response.put(key, "username");
        });
    }

}
