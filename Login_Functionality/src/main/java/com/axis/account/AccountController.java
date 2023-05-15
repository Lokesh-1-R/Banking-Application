package com.axis.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axis.account.Exceptions.AccountNotClearedException;
import com.axis.account.Exceptions.ResourceNotFoundException;
import com.axis.config.JwtService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    @Autowired
    public AccountController(AccountService accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    /**
     * This controller fetches the user account overview by getting the userId from the JWT token
     */
//    @GetMapping("/overview")
//    public ResponseEntity<ApiResponse> getUserAccountOverview(
//                    @RequestHeader("Authorization") String jwt) {
//        try {
//            AccountOverviewResponse response = accountService.generateAccountOverviewByUserId(
//                    jwtService.extractUsername(jwt));
//            return new ResponseEntity<>(new ApiResponse("user account overview", response),
//                    HttpStatus.OK);
//        } catch (ResourceNotFoundException e) {
//            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
//        }
//
//    }
//    
    /**
     * This controller allows user to close their account by getting the userId from the JWT and the relieving reason
     * from the request body
     */
//    @DeleteMapping("/close")
//    public ResponseEntity<ApiResponse> closeAccount(
//            @RequestHeader("Authorization") String jwt) {
//            accountService.closeAccount(jwtService.extractUsername(jwt));
//            return new ResponseEntity<>(new ApiResponse("account closed successfully"), HttpStatus.OK);
//    }
    @PostMapping("/profile")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        accountService.createAccount(account);
        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }
    @GetMapping("get/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Account> getAccountByUserId(@PathVariable("id") String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") String accontNumber, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(accontNumber, account);
        if (updatedAccount != null) {
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") String  accountNumber) throws AccountNotClearedException  {
        accountService.closeAccount(accountNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
