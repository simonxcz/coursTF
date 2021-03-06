package fr.flaurens.bankaccount.services;

import fr.flaurens.bankaccount.adapters.AccountDAO;
import fr.flaurens.bankaccount.adapters.OperationDAO;
import fr.flaurens.bankaccount.model.Account;
import fr.flaurens.bankaccount.model.Operation;
import fr.flaurens.bankaccount.model.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class AccountService {

    private final AccountDAO accountDAO;

    private final OperationDAO operationDAO;

    public AccountService(@Autowired AccountDAO accountDAO, @Autowired OperationDAO operationDAO){
        this.accountDAO = accountDAO;
        this.operationDAO = operationDAO;
    }

    @Transactional
    public float makeDepositOnAccount(long accountId, float amount){
        Account workingAccount = this.accountDAO.getAccountById(accountId);
        Operation operation = new Operation(accountId, amount, OperationType.DEPOSIT);
        operationDAO.persistOperation(operation);
        return workingAccount.updateBalance(amount);
    }

    @Transactional
    public float retrieveFromAccount(long accountId, float amount){
        Account workingAccount = this.accountDAO.getAccountById(accountId);
        Operation operation = new Operation(accountId, amount, OperationType.WITHDRAWAL);
        operationDAO.persistOperation(operation);
        return workingAccount.updateBalance(-amount);
    }

    public List<Operation> getAccountHistory(long accountId){
        return operationDAO.getOperationByAccount(accountId);
    }


    public float getCurrentBalance(long accountId){
        Account workingAccount = this.accountDAO.getAccountById(accountId);
        return workingAccount.getBalance();
    }
}
