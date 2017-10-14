package server.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.error.Error;

import javax.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:8080", "https://tp-2017-2-caesar.herokuapp.com"})
@RequestMapping("/api")
@RestController
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/auth/signup")
    public ResponseEntity register(@RequestBody Account account, HttpSession httpSession) {

        ResponseEntity responseEntity = accountService.createAccount(account);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        account = (Account) responseEntity.getBody();

        httpSession.setAttribute("username", account.getUsername());

        return responseEntity;
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity authorize(@RequestBody Account account, HttpSession httpSession) {

        String usernameBySession = (String) httpSession.getAttribute("username");

        if (usernameBySession != null) {
            return new ResponseEntity(Error.getJson("You have already authorized"),
                    HttpStatus.I_AM_A_TEAPOT); // http response code 418
        }

//      **************************************find account**************************************
        ResponseEntity responseEntity = accountService.getAccount(account.getUsername());
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
//      **************************************find account**************************************

        String username = account.getUsername();
        String password = account.getPassword();

        if (!accountService.checkPassword(username, password)) {
            return new ResponseEntity(Error.getJson("Wrong password! Check CapsLock :) and try again."),
                    HttpStatus.FORBIDDEN); // http response code 403
        }

        httpSession.setAttribute("username", username);

        return responseEntity;
    }

    @GetMapping(path = "/auth/me")
    public ResponseEntity requestUserCurrentSession(HttpSession httpSession) {

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            return new ResponseEntity(Error.getJson("You haven't authorized."),
                    HttpStatus.UNAUTHORIZED); // http response code 401
        }

        return accountService.getAccount(username);
    }

    @GetMapping(path = "/auth/logout")
    public ResponseEntity logOut(HttpSession httpSession) {

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            return new ResponseEntity(Error.getJson("You haven't authorized."),
                    HttpStatus.UNAUTHORIZED);
        }

        httpSession.removeAttribute("username");
        httpSession.invalidate();

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/user/rename")
    public ResponseEntity rename(@RequestBody Account account, HttpSession httpSession) {

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            return new ResponseEntity(Error.getJson("You must authorize" +
                    " before change your personal Information"),
                    HttpStatus.UNAUTHORIZED);
        }

        ResponseEntity responseEntity = accountService.renameAccount(account, username);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        account = (Account) responseEntity.getBody();

        httpSession.setAttribute("username", account.getUsername());

        return responseEntity;
    }

    @GetMapping(path = "/user/rating")
    public ResponseEntity printAllUsers() {
        return accountService.getAccountsScore();
    }

}



