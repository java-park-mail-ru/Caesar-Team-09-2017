package technopark.services.dao;

import technopark.account.dao.AccountDao;

import java.util.List;

public interface AccountDaoInterface {
    AccountDao insertAccount(AccountDao accountDao);

    AccountDao getAccount(String username);

    AccountDao renameAccount(AccountDao accountDao, String username);

    AccountDao getAccountId(long id);

    void setScore(AccountDao accountDao, int score);

    List<AccountDao> getScoreAccount();
}
