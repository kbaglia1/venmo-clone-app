package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate =  new JdbcTemplate(dataSource);
    }

    @Override
    public Account getByUserId(long userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public boolean transferFunds(BigDecimal transferAmount, long fromAccountId, long toAccountId) {
        jdbcTemplate.execute("BEGIN TRANSACTION;");

        try {
            this.deposit(toAccountId, transferAmount);
            this.withdraw(fromAccountId, transferAmount);
            jdbcTemplate.execute("COMMIT;");

        } catch (DataAccessException e) {
            jdbcTemplate.execute("ROLLBACK");
            return false;
        }

        return true;
    }

    @Override
    public void deposit(long toAccountId, BigDecimal transferAmount) {
        String sql = "UPDATE accounts SET balance = balance + ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferAmount, toAccountId);
    }

    @Override
    public void withdraw(long fromAccountId, BigDecimal transferAmount) {
        String sql = "UPDATE accounts SET balance = balance - ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferAmount, fromAccountId);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setId(rowSet.getLong("account_id"));
        account.setUserId(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
