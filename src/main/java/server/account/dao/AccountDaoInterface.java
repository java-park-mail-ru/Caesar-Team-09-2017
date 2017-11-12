package server.account.dao;

import java.util.List;

public interface AccountDaoInterface {
    AccountDao insertAccount(AccountDao accountDao);

    AccountDao getAccount(String username);

    AccountDao renameAccount(AccountDao accountDao, String username);

    List<AccountDao> getScoreAccount();
}
