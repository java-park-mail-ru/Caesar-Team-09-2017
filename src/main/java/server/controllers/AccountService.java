package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.JsonParser;
import server.UsersProfile;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class AccountService {

    private UsersProfile usersProfile = new UsersProfile();


    @RequestMapping(method = RequestMethod.POST, path = "registr")
    public ResponseEntity register(@RequestBody JsonParser jsonParser) { // как поймать исключения в конструкторе?

        String email = jsonParser.getEmail();
        String username = jsonParser.getUsername();
        String password = jsonParser.getPassword();

        Map<String, String> response = new LinkedHashMap<>();

        if (usersProfile.containsKeyEmail(email)) {
            response.put("Cause", "This email \"" + email + "\" exists");
            response.put("registration", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // http response code 400
        }

        if (usersProfile.containsKeyUsername(username)) {
            response.put("Cause", "This username \"" + username + "\" exists");
            response.put("registration", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // http response code 400
        }

        if (validation(email, username, password, response)) {
            usersProfile.setUsersMail(username, email);
            usersProfile.setMailUsername(username, email);
            usersProfile.setUsers(username, password);

            response.put("registration", "SUCCESSFUL");
            return ResponseEntity.ok(response);  // http response code 200
        } else {
            response.put("registration", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // http response code 400
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "auth")
    public ResponseEntity authorize(@RequestBody JsonParser jsonParser, HttpSession httpSession) {

        Map<String, String> response = new LinkedHashMap<>();

        String username = jsonParser.getUsername();
        String password = jsonParser.getPassword();

        Integer id = (Integer) httpSession.getAttribute("sessionId");

        if (id != null) {
            response.put("Cause", "You are already authorized");
            response.put("authorization", "ERROR");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(response); // http response code 418
        }

        if (!usersProfile.containsKeyUsername(username)) {
            response.put("Cause", " \"" + username + "\" not registrated :( " +
                    "register: {\"email\",\"username\",\"password\"} on POST localhost:8081/registr");
            response.put("authorization", "ERROR");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // http response code 403
        } else if (!password.equals(usersProfile.getPassword(username))) {
            response.put("Cause", "Wrong password! Check CapsLock :) and try again.");
            response.put("authorization", "ERROR");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }


        if (httpSession.getAttribute("sessionId") == null) {
            id = usersProfile.getSessionId();
            usersProfile.incrementSessionId();
            httpSession.setAttribute("sessionId", id);
            httpSession.setAttribute("username", username);
        }

        response.put("username", "Hello " + username + "!");
        response.put("authorization", "SUCCESSFUL");

        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "info")
    public ResponseEntity requestUserCurrentSession(HttpSession httpSession) {

        Map<String, String> response = new LinkedHashMap<>();
        Integer id = (Integer) httpSession.getAttribute("sessionId");
        String username = (String) httpSession.getAttribute("username");

        if (id == null) {
            response.put("Cause", "You haven't authorized. authorize: {\"username\",\"password\"} on POST localhost:8081/auth");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // http response code 401
        } else {
            response.put("username", "Hello " + username);
            response.put("sessionId", "Your sessionId: " + id.toString());
            response.put("info", "SUCCESSFUL");
        }

        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "exit")
    public ResponseEntity logOut(HttpSession httpSession) {

        Map<String, String> response = new LinkedHashMap<>();
        Integer id = (Integer) httpSession.getAttribute("sessionId");
        @SuppressWarnings("unused")
        String username = (String) httpSession.getAttribute("username");

        if (id == null) {
            response.put("Cause", "You haven't authorized. authorize: {\"username\",\"password\"} on POST localhost:8081/auth");
            response.put("log out", "ERROR");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        httpSession.removeAttribute("sessionId");
        httpSession.removeAttribute("username");

        response.put("log out", "SUCCESSFUL");

        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "rename")
    public ResponseEntity rename(@RequestBody JsonParser jsonParser, HttpSession httpSession) {

        Map<String, String> response = new LinkedHashMap<>();

        String email = jsonParser.getEmail();
        String username = jsonParser.getUsername();
        String password = jsonParser.getPassword();

        Integer id = (Integer) httpSession.getAttribute("sessionId");
        String oldUsername = (String) httpSession.getAttribute("username");

        if (id == null) {
            response.put("authorization", "ERROR");
            response.put("Cause", "You must authorize before change your personal Information\n");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (validation(email, username, password, response)) {
            usersProfile.removeUser(oldUsername);

            usersProfile.setUsersMail(username, email);
            usersProfile.setMailUsername(username, email);
            usersProfile.setUsers(username, password);

            response.put("rename", "SUCCESSFUL");
            return ResponseEntity.ok(response);
        } else {
            response.put("rename", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "allUsers")
    public ResponseEntity printAllUsers() {

        Map<String, String> response = new LinkedHashMap<>();

        usersProfile.getAllUsers(response);

        if (response.isEmpty()) {
            response.put("all users", "not users yet");
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(response); // 416
        }

        return ResponseEntity.ok(response);

    }

    private boolean validation(String email, String username, String password, Map<String, String> response) {
        if (email == null) {
            email = "Uncorrect format. Use this format for email: \"email\":\"yourEmail@mail.ru\"";
            response.put("Cause", email);
            return false;
        }

        if (username == null) {
            username = "Uncorrect format. Use this format for username: \"username\":\"yourUsername\"";
            response.put("Cause", username);

            return false;
        }

        if (password == null) {
            password = "Uncorrect format. Use this format for password: \"password\":\"yourPassword\"";
            response.put("Cause", password);

            return false;
        }

        return true;
    }

}






