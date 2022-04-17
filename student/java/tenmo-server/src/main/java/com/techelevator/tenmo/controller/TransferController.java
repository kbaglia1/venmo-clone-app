package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/transfer/")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public TransferDTO getById(@PathVariable long id) {
        return transferDao.getTransferDTO(id);
    }

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<User> listAllUsers() {
        return userDao.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer sendTransfer(@Valid @RequestBody Transfer transfer) {
        long accountFromId = accountDao.getByUserId(transfer.getFromUserId()).getId();
        long accountToId = accountDao.getByUserId(transfer.getToUserId()).getId();
        transfer.setFromAccountId(accountFromId);
        transfer.setToAccountId(accountToId);
        Transfer newTransfer = transferDao.create(transfer);
        accountDao.transferFunds(newTransfer.getAmount(), accountFromId, accountToId);
        return newTransfer;
    }

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public List<List<TransferDTO>> listUserTransactions(Principal principal) {
        List<List<TransferDTO>> listList = new ArrayList<>();
        long accountId = accountDao.getByUserId(userDao.findIdByUsername(principal.getName())).getId();
        listList.add(transferDao.listSentTransfers(principal, accountId));
        listList.add(transferDao.listReceivedTransfers(principal, accountId));
        return listList;
    }

}
