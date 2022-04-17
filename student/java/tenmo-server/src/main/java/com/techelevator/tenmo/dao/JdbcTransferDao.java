package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getById(long id) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public TransferDTO getTransferDTO(long id) {
        TransferDTO transfer = null;
        String sql = "SELECT transfer_id, username AS to_user, " +
                "(SELECT username FROM transfers " +
                "JOIN accounts ON account_from = account_id " +
                "JOIN users on users.user_id = accounts.user_id " +
                "WHERE transfer_id = ?) AS from_user, " +
                "transfer_type_id, transfer_status_id, amount FROM transfers " +
                "JOIN accounts ON account_to = account_id " +
                "JOIN users on users.user_id = accounts.user_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        if (results.next()) {
            transfer = mapRowToTransferDTO(results);
            transfer.setToUsername(results.getString("to_user"));
            transfer.setFromUsername(results.getString("from_user"));
        }
        return transfer;
    }

    @Override
    public List<TransferDTO> listSentTransfers(Principal principal, long id) {
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, username, transfer_type_id, transfer_status_id, amount FROM transfers " +
                "JOIN accounts ON account_to = account_id " +
                "JOIN users on users.user_id = accounts.user_id " +
                "WHERE account_from = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            TransferDTO transferDTO = new TransferDTO();
            transferDTO = mapRowToTransferDTO(results);
            transferDTO.setFromUsername(principal.getName());
            transferDTO.setToUsername(results.getString("username"));
            transferList.add(transferDTO);
        }
        return transferList;
    }

    @Override
    public List<TransferDTO> listReceivedTransfers(Principal principal, long id) {
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, username, transfer_type_id, transfer_status_id, amount FROM transfers " +
                "JOIN accounts ON account_from = account_id " +
                "JOIN users on users.user_id = accounts.user_id " +
                "WHERE account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            TransferDTO transferDTO = new TransferDTO();
            transferDTO = mapRowToTransferDTO(results);
            transferDTO.setToUsername(principal.getName());
            transferDTO.setFromUsername(results.getString("username"));
            transferList.add(transferDTO);
        }
        return transferList;
    }

    @Override
    public Transfer create(Transfer transfer) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Long newTransferId = null;
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Long.class, transfer.getTypeId(), transfer.getStatusId(),
                    transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getAmount());
        } catch (DataAccessException e) {
            System.out.println("An error occurred. Please retry with valid info.");
        }
        return getById(newTransferId);
    }


    @Override
    public void updateStatus(Transfer transfer) {

    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setId(rowSet.getLong("transfer_id"));
        transfer.setTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setFromAccountId(rowSet.getLong("account_from"));
        transfer.setToAccountId(rowSet.getLong("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

    private TransferDTO mapRowToTransferDTO(SqlRowSet rowSet) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(rowSet.getLong("transfer_id"));
        transferDTO.setTypeId(rowSet.getInt("transfer_type_id"));
        transferDTO.setStatusId(rowSet.getInt("transfer_status_id"));
        transferDTO.setAmount(rowSet.getBigDecimal("amount"));
        return transferDTO;
    }

}
