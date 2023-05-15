package com.axis.account;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.axis.account.Exceptions.AccountNotActivatedException;
import com.axis.account.Exceptions.AccountNotClearedException;
import com.axis.account.Exceptions.ResourceNotFoundException;
import com.axis.user.User;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Async
    public void createAccount(Account account) {
        account.setAccountNumber(generateUniqueAccountNumber());
        if(account.getAccountType()==AccountType.CURRENT_ACCOUNT) {
            account.setAccountType(AccountType.CURRENT_ACCOUNT);

        }else {
            account.setAccountType(AccountType.SAVINGS_ACCOUNT);
	
        }
        account.setAccountStatus(AccountStatus.ACTIVATED);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        
        
        accountRepository.save(account);
    }
    private String generateUniqueAccountNumber() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int accountNumber;
        boolean exists;
        do {
            accountNumber = random.nextInt(1_000_000_000);
            exists = accountRepository.existsByAccountNumber(String.format("%09d", accountNumber));
        } while (exists);
        return String.format("%09d", accountNumber);
    }
    public Account getAccountByAccountNumber(String accountNumber) {
        Optional<Account> account = accountRepository.findAccountByAccountNumber(accountNumber);
        if(!account.isPresent()){
            throw new ResourceNotFoundException("account not found");
        }
        return account.get();
    }

    public AccountOverviewResponse generateAccountOverviewByUserId(String accountNumber){
        Account userAccount = getAccountByAccountNumber(accountNumber);
        return new AccountOverviewResponse(
                userAccount.getAccountBalance(),
                userAccount.getAccountNumber(),
                userAccount.getAccountType().name(),
                userAccount.getAccountStatus().name()
        );
    }
    public Account updateAccount(String accountNumber , Account existingAccount) {
    	
    	Account acc = accountRepository.findAccountByAccountNumber(accountNumber).get();
    	acc.setAccountBalance(existingAccount.getAccountBalance());
    	acc.setAccountStatus(existingAccount.getAccountStatus());
    	acc.setAccountType(existingAccount.getAccountType());
        acc.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(acc);
        
        return acc;
    }
    
    public void closeAccount(String accountNumber) throws AccountNotClearedException{
        Account userAccount = getAccountByAccountNumber(accountNumber);
//        if(!noPendingOrAvailableFundInTheAccount(userAccount)) {

        if(noPendingOrAvailableFundInTheAccount(userAccount)) {
            throw new AccountNotClearedException("confirm there is no pending or available balance in the account");
        }
        userAccount.setAccountStatus(AccountStatus.CLOSED);
        updateAccount(userAccount.getAccountNumber(),userAccount);
    }
    private boolean noPendingOrAvailableFundInTheAccount(Account account){
        return account.getAccountBalance().equals(BigDecimal.ZERO);
    }
    
    public void creditAccount(Account receiverAccount,BigDecimal amount) {
        receiverAccount.setAccountBalance(receiverAccount.getAccountBalance().add(amount));
        updateAccount(receiverAccount.getAccountNumber(),receiverAccount);
    }
    public Account accountExistsAndIsActivated(String accountNumber) throws AccountNotActivatedException{
        Optional<Account> exitingAccount = accountRepository.findAccountByAccountNumber(accountNumber);
        if(exitingAccount.isPresent()){
            if(exitingAccount.get().getAccountStatus().equals(AccountStatus.ACTIVATED)){
                return exitingAccount.get();
            }
            throw new AccountNotActivatedException("Account not activated");
        }
        throw new ResourceNotFoundException("Account not found");
    }

	public List<Account> getAllAccounts() {
		// TODO Auto-generated method stub
		return accountRepository.findAll();
	}
	
}
