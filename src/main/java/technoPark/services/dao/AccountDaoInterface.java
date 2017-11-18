package technoPark.services.dao;

import technoPark.model.account.dao.AccountDao;

import java.util.List;

public interface AccountDaoInterface {
    AccountDao insertAccount(AccountDao accountDao);

    AccountDao getAccount(String username);

    AccountDao renameAccount(AccountDao accountDao, String username);

    AccountDao getAccountId(long id);

    List<AccountDao> getScoreAccount();
}
