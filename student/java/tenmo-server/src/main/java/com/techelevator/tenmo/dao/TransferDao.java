package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    Transfer getById(long id);

    TransferDTO getTransferDTO(long id);

    List<TransferDTO> listSentTransfers(Principal principal, long id);

    List<TransferDTO> listReceivedTransfers(Principal principal , long id);

    Transfer create(Transfer transfer);

    void updateStatus(Transfer transfer);

}
