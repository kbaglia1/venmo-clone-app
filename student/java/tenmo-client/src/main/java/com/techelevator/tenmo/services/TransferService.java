package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public TransferDTO getTransferById(long id) {
        ResponseEntity<TransferDTO> response = null;
        try {
            response = restTemplate.exchange(baseUrl + "transfer/" + id,
                    HttpMethod.GET, makeAuthEntity(),TransferDTO.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return response.getBody();
    }

    public User[] listAllUsers() {
        ResponseEntity<User[]> response = null;
        try {
            response = restTemplate.exchange(baseUrl + "transfer/users",
                    HttpMethod.GET, makeAuthEntity(), User[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return response.getBody();
    }

    public Transfer sendTransfer(Transfer transfer) {
        Transfer newTransfer = null;
         try {
             ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/", HttpMethod.POST,
                     makeTransferEntity(transfer), Transfer.class);
             newTransfer = response.getBody();
         } catch (RestClientResponseException | ResourceAccessException e) {
             System.out.println(e.getMessage());
         }
         return newTransfer;
    }

    public TransferDTO[][] listUserTransfers() {
        ResponseEntity<TransferDTO[][]> response = null;
        try {
            response = restTemplate.exchange(baseUrl + "transfer/user",
                    HttpMethod.GET, makeAuthEntity(), TransferDTO[][].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return response.getBody();
    }



    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
