package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.User;
import server.UserService;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class AccountController {

    private UserService userService = new UserService();
    private static final String FRONTEND_ORIGIN = "http://tp-2017-2-caesar.herokuapp.com";

    @CrossOrigin(origins = FRONTEND_ORIGIN)
    @RequestMapping(method = RequestMethod.POST, path = "reg")
    public ResponseEntity register(@RequestBody User user) {

        String username = user.getUsername();

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        if (userService.validation(user, bodyResponse)) {
            userService.setUser(username, user);

            return ResponseEntity.ok(user);  // http response code 200
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse); // http response code 400
        }

    }

    @CrossOrigin(origins = FRONTEND_ORIGIN)
    @RequestMapping(method = RequestMethod.POST, path = "auth")
    public ResponseEntity authorize(@RequestBody User user, HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = user.getUsername();
        String password = user.getPassword();

        String usernameBySession = (String) httpSession.getAttribute("username");

        if (usernameBySession != null) {
            bodyResponse.put("Cause", "You are already authorized");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(bodyResponse); // http response code 418
        }

        if (!userService.containsUsername(username)) {
            bodyResponse.put("Cause", " \"" + username + "\"did not registrate :( ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bodyResponse); // http response code 403
        }

        if (!password.equals(userService.getPassword(username))) {
            bodyResponse.put("Cause", "Wrong password! Check CapsLock :) and try again.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bodyResponse);
        }

        if (httpSession.getAttribute("username") == null) {
            httpSession.setAttribute("username", username);
        }

        return ResponseEntity.ok(bodyResponse);
    }

    @CrossOrigin(origins = FRONTEND_ORIGIN)
    @RequestMapping(method = RequestMethod.POST, path = "info")
    public ResponseEntity requestUserCurrentSession(HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            bodyResponse.put("Cause", "You haven't authorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyResponse); // http response code 401
        }

        bodyResponse.put("username", "Hello " + username);
        bodyResponse.put("sessionId", "Your sessionId: " + httpSession.getId());

        return ResponseEntity.ok(bodyResponse);
    }

    @CrossOrigin(origins = FRONTEND_ORIGIN)
    @RequestMapping(method = RequestMethod.POST, path = "exit")
    public ResponseEntity logOut(HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            bodyResponse.put("Cause", "You haven't authorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyResponse);
        }

        httpSession.removeAttribute("username");

        return ResponseEntity.ok(bodyResponse);
    }

    @CrossOrigin(origins = FRONTEND_ORIGIN)
    @RequestMapping(method = RequestMethod.POST, path = "rename")
    public ResponseEntity rename(@RequestBody User user, HttpSession httpSession) {

        Map<String, String> bodyResponse = new LinkedHashMap<>();

        String username = user.getUsername();

        String oldUsername = (String) httpSession.getAttribute("username");

        if (oldUsername == null) {
            bodyResponse.put("Cause", "You must authorize before change your personal Information\n");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bodyResponse);
        }

        if (userService.validation(user, bodyResponse)) {
            userService.removeUser(oldUsername);

            userService.setUser(username, user);
            httpSession.setAttribute("username", username);
            return ResponseEntity.ok(bodyResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse);
        }

    }

    @CrossOrigin(origins = FRONTEND_ORIGIN)
    @RequestMapping(method = RequestMethod.POST, path = "allUsers")
    public ResponseEntity printAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
