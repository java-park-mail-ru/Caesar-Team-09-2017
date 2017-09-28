package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.User;
import server.UserService;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:8080", "https://tp-2017-2-caesar.herokuapp.com"})
@RestController
public class AccountController {

    private UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/signup")
    public ResponseEntity register(@RequestBody User user, HttpSession httpSession) {

        String username = user.getUsername();

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        if (userService.validation(user, bodyResponse)) {
            userService.setUser(username, user);
            authorize(user, httpSession);
            return ResponseEntity.ok(user);  // http response code 200
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse); // http response code 400
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/login")
    public ResponseEntity authorize(@RequestBody User user, HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = user.getUsername();
        String password = user.getPassword();

        String usernameBySession = (String) httpSession.getAttribute("username");

        if (usernameBySession != null) {
            bodyResponse.put("Cause", "You have already authorized");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(bodyResponse); // http response code 418
        }

        if (!userService.containsUsername(username)) {
            bodyResponse.put("Cause", " \"" + username + "\"did not registrate :( ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse);
        }

        if (!password.equals(userService.getPassword(username))) {
            bodyResponse.put("Cause", "Wrong password! Check CapsLock :) and try again.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bodyResponse);  // http response code 403
        }

        if (httpSession.getAttribute("username") == null) {
            httpSession.setAttribute("username", username);
        }

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/info")
    public ResponseEntity requestUserCurrentSession(HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            bodyResponse.put("Cause", "You haven't authorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyResponse); // http response code 401
        }

        bodyResponse.put("username", username);
        bodyResponse.put("email", userService.getEmail(username));

        return ResponseEntity.ok(bodyResponse);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/logout")
    public ResponseEntity logOut(HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            bodyResponse.put("Cause", "You haven't authorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyResponse);
        }

        httpSession.removeAttribute("username");
        httpSession.invalidate();

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/user/rename")
    public ResponseEntity rename(@RequestBody User user, HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = user.getUsername();

        String oldUsername = (String) httpSession.getAttribute("username");

        if (oldUsername == null) {
            bodyResponse.put("Cause", "You must authorize before change your personal Information\n");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyResponse);
        }

        if (userService.validation(user, bodyResponse)) {
            userService.removeUser(oldUsername);

            userService.setUser(username, user);
            httpSession.setAttribute("username", username);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/user/rating")
    public ResponseEntity printAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}



