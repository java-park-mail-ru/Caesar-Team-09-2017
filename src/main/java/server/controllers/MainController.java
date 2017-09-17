package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;


@RestController
public class MainController {

    private int id = 0;
    private HashMap<String, String>  users;

    private HashMap<String, String>  usersMail;
    private HashMap<String, String>  mailUsernameFind;

    private HashMap<String, Integer> usersSession;
    private HashMap<Integer, String> sessionUsernameFind;


    MainController() {
        users = new HashMap<>();

        usersMail = new HashMap<>();
        mailUsernameFind = new HashMap<>();

        usersSession = new HashMap<>();
        sessionUsernameFind = new HashMap<>();
    }

    @RequestMapping(method = RequestMethod.POST, path = "registration")
    public ResponseEntity register(@RequestBody JsonParser jsonParser) { // как поймать исключения в конструкторе?

        String email = jsonParser.getEmail();
        String username = jsonParser.getUsername();
        String password = jsonParser.getPassword();

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if (usersMail.containsKey(email)) {
            map.put("Cause", "This email \"" + email + "\" exists");
            map.put("registration", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map); // http response code 400
        }

        if (users.containsKey(username)) {
            map.put("Cause", "This username \"" + username + "\" exists");
            map.put("registration", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map); // http response code 400
        }

        if (validation(email,username,password,map) == true) {
            usersMail.put(email, username);
            mailUsernameFind.put(username, email);
            users.put(username, password);
            map.put("registration", "SUCCESSFUL");
            return ResponseEntity.ok(map);  // http response code 200
        } else {
            map.put("registration", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map); // http response code 400
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "auth")
    public ResponseEntity authorize(@RequestBody JsonParser jsonParser, HttpSession httpSession)  {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        String username = jsonParser.getUsername();
        String password = jsonParser.getPassword();

        if (usersSession.containsKey(username)) {
            map.put("Cause", "You are already authorized");
            map.put("authorization", "ERROR");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(map); // http response code 418
        }

        if (users.containsKey(username) == false) {
            map.put("Cause"," \"" + username + "\" not registrated :( register: {\"email\",\"username\",\"password\"} on POST localhost:8081/registration");
            map.put("authorization", "ERROR");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map); // http response code 403
        } else if (password.equals(users.get(username)) == false) {
            map.put("Cause","Wrong password! Check CapsLock :) and try again.");
            map.put("authorization", "ERROR");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }

        Integer id = (Integer) httpSession.getAttribute("sessionId");
        if ( id == null) {  // пользователь заходит впервые за долгое время
            id = this.id++;
            httpSession.setAttribute("sessionId", 0);
        }
//        Пользователь зашел под другим аккаунтом или если включен режим Collect Cookies. id не меняется, меняется активный user
        String oldUsername = sessionUsernameFind.get(id);
        usersSession.remove(oldUsername);
        sessionUsernameFind.remove(id);
//        Если этот режим выключен вверхние 3 строчки можно комментить для оптимизации
        usersSession.put(username,id);
        sessionUsernameFind.put(id,username);

        map.put("username","Hello " + sessionUsernameFind.get(id) + "!");
        map.put("authorization", "SUCCESSFUL");


        return ResponseEntity.ok(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "info")
    public ResponseEntity RequestUserCurrentSession(HttpSession httpSession) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Integer id = (Integer) httpSession.getAttribute("sessionId");

        if ( id == null) {
            map.put("Cause", "You haven't authorized. authorize: {\"username\",\"password\"} on POST localhost:8081/auth");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map); // http response code 401
        } else {
            map.put("username", "Hello " + sessionUsernameFind.get(id));
            map.put("sessionId", "Your sessionId: " + id.toString());
            map.put("info", "SUCCESSFUL");
        }

        return ResponseEntity.ok(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "exit")
    public ResponseEntity logOut(HttpSession httpSession)  {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Integer id = (Integer) httpSession.getAttribute("sessionId");

        if ( id == null) {
            map.put("Cause", "You haven't authorized. authorize: {\"username\",\"password\"} on POST localhost:8081/auth");
            map.put("log out", "ERROR");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }

        httpSession.removeAttribute("sessionId");
        String username = sessionUsernameFind.get(id);
        usersSession.remove(username);
        sessionUsernameFind.remove(id);

        map.put("log out", "SUCCESSFUL");

        return ResponseEntity.ok(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "rename")
    public ResponseEntity rename(@RequestBody JsonParser jsonParser, HttpSession httpSession)  {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        String email = jsonParser.getEmail();
        String username = jsonParser.getUsername();
        String password = jsonParser.getPassword();

        Integer id = (Integer) httpSession.getAttribute("sessionId");
        if (id == null) {
            map.put("authorization", "ERROR");
            map.put("Cause", "You must authorize before change your personal Information\n");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }

        if (validation(email,username,password,map) == true) {
//            Находим пользователя по sessionId
            String oldUsername = sessionUsernameFind.get(id);
            String oldEmail = mailUsernameFind.get(oldUsername);
            String oldPassword = users.get(oldUsername);
//            Удаляем старые записи
            usersMail.remove(oldEmail);
            mailUsernameFind.remove(username);
            users.remove(username);
            usersSession.remove(username);
            sessionUsernameFind.remove(id);
//            Запоминаем новые
            usersMail.put(email, username);
            mailUsernameFind.put(username, email);
            users.put(username, password);
            usersSession.put(username,id);
            sessionUsernameFind.put(id,username);

            map.put("rename", "SUCCESSFUL");
            return ResponseEntity.ok(map);  // http response code 200
        } else {
            map.put("rename", "ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map); // http response code 400
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "allUsers")
    public ResponseEntity printAllUsers()  {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        users.forEach((key, value) -> {
            map.put(mailUsernameFind.get(key), "email");
            map.put(key, "username");
            if (usersSession.containsKey(key)) {
                map.put(usersSession.get(key).toString(), "sessionId " + key);
            } else {
                map.put("sessionId " + key, "not authorized");
            }
        });

        return ResponseEntity.ok(map);

    }

    public boolean validation(String email, String username, String password, LinkedHashMap<String, String> map) {
        if (email == null) {
            email = "Uncorrect format. Use this format for email: \"email\":\"yourEmail@mail.ru\"";
            map.put("Cause", email);
            return false;
        }

        if (username == null) {
            username = "Uncorrect format. Use this format for username: \"username\":\"yourUsername\"";
            map.put("Cause", username);

            return false;
        }

        if (password == null) {
            password = "Uncorrect format. Use this format for password: \"password\":\"yourPassword\"";
            map.put("Cause", password);

            return false;
        }

        return true;
    }
}






