package server.account;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.account.dao.AccountDao;
import server.error.Error;

import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080", "https://tp-2017-2-caesar.herokuapp.com"})
@RequestMapping("/api")
@RestController
public class AccountController {

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/auth/signup")
    public ResponseEntity register(@RequestBody Account account, HttpSession httpSession) {

        AccountDao accountDao = accountService.createAccount(account);
        if (!accountDao.getStatus().equals("CREATED")) {
            logger.info(accountDao.getStatus());
            return new ResponseEntity(Error.getJson(accountDao.getError()),
                    HttpStatus.CONFLICT);
        }

        httpSession.setAttribute("username", accountDao.getUsername());

        return new ResponseEntity(accountDao, HttpStatus.CREATED); // 201
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity authorize(@RequestBody Account account, HttpSession httpSession) {

        String usernameBySession = (String) httpSession.getAttribute("username");

        if (usernameBySession != null) {
            return new ResponseEntity(Error.getJson("You have already authorized"),
                    HttpStatus.I_AM_A_TEAPOT); // http response code 418
        }

        String username = account.getUsername();
        AccountDao accountDao = accountService.getAccount(username);
        if (!accountDao.getStatus().equals("OK")) {
            logger.info(accountDao.getStatus());
            return new ResponseEntity(Error.getJson(accountDao.getError()),
                    HttpStatus.NOT_FOUND);
        }

        String password = account.getPassword();

        if (!accountService.checkPassword(username, password)) {
            logger.warn("Wrong password");
            return new ResponseEntity(Error.getJson("Wrong password! Check CapsLock :) and try again."),
                    HttpStatus.FORBIDDEN); // http response code 403
        }

        httpSession.setAttribute("username", username);

        return new ResponseEntity(accountDao, HttpStatus.OK); // 200
    }

    @GetMapping(path = "/auth/me")
    public ResponseEntity requestUserCurrentSession(HttpSession httpSession) {

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            return new ResponseEntity(Error.getJson("You haven't authorized."),
                    HttpStatus.UNAUTHORIZED); // http response code 401
        }

        AccountDao accountDao = accountService.getAccount(username);

        return new ResponseEntity(accountDao, HttpStatus.OK);
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
            return new ResponseEntity(Error.getJson("You must authorize"
                    + " before change your personal Information"),
                    HttpStatus.UNAUTHORIZED);
        }

        AccountDao accountDao = accountService.renameAccount(account, username);
        if (!accountDao.getStatus().equals("OK")) {
            logger.info(accountDao.getStatus());
            HttpStatus httpStatus;
            if (accountDao.getStatus().equals("NOT_FOUND")) {
                httpStatus = HttpStatus.NOT_FOUND;
            } else if (accountDao.getStatus().equals("CONFLICT")) {
                httpStatus = HttpStatus.CONFLICT;
            } else {
                httpStatus = HttpStatus.BAD_REQUEST;
            }
            return new ResponseEntity(Error.getJson(accountDao.getError()),
                    httpStatus);
        }

        httpSession.setAttribute("username", accountDao.getUsername());

        return new ResponseEntity(accountDao, HttpStatus.OK);
    }

    @GetMapping(path = "/user/rating")
    public ResponseEntity printAllUsers() {
        List<AccountDao> accountsDao = accountService.getAccountsScore();

        return new ResponseEntity(accountsDao, HttpStatus.OK);
    }

}



