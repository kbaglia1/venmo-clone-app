package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.TimerTask;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService,
			   TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;

	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {

		System.out.println("***********************************");
    	System.out.println("Your current balance is: " +
				accountService.getAccount(currentUser.getUser().getId()).getBalance());
		System.out.println("***********************************");
	}

	private void viewTransferHistory() {
		TransferDTO[][] allTransfers = transferService.listUserTransfers();

		displaySentTransfersBanner();
		for (TransferDTO transfer : allTransfers[0]) {
			System.out.println(transfer.getId() + "        "
					+ transfer.getToUsername() + "           "
					+ transfer.getAmount());
		}

		System.out.println("");

		displayReceivedTransfersBanner();
		for (TransferDTO transfer : allTransfers[1]) {
			System.out.println(transfer.getId() + "        "
					+ transfer.getFromUsername() + "           "
					+ transfer.getAmount());
		}


		long transferId = console.getUserInputInteger("Please enter a transfer ID to view details. (0 to cancel)");
		TransferDTO transferDTO = transferService.getTransferById(transferId);
		displayTransferDetailsBanner();
		System.out.println("ID: " + transferDTO.getId());
		System.out.println("From: " + transferDTO.getFromUsername());
		System.out.println("To: " + transferDTO.getToUsername());
		System.out.println("Type: " + getType(transferDTO.getTypeId()));
		System.out.println("Status: " + getStatus(transferDTO.getStatusId()));
		System.out.println("Amount: $" + transferDTO.getAmount());

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
    	displayUsers();
		Transfer newTransfer = new Transfer();
		newTransfer.setFromUserId(currentUser.getUser().getId());
		newTransfer.setToUserId(console.getUserInputInteger("Please enter the User ID you wish to send to"));
		newTransfer.setTypeId(2);
		boolean transferCheck = false;
		while (!transferCheck) {
			BigDecimal amount = new BigDecimal(console.getUserInput("Please enter the transfer amount ($$$.00)"));

			if ( amount.compareTo(accountService.getAccount(currentUser.getUser().getId()).getBalance()) <= 0){
				newTransfer.setAmount(amount);
				transferCheck = true;
				System.out.println("You just sent " + amount + " to " + newTransfer.getToUserId());
				System.out.println("They must be thrilled.\n");
			}
			if (!transferCheck) {
				System.out.println("You cannot send more TE Bucks than you have in your account.\n");
				viewCurrentBalance();
			}
		}
		newTransfer.setStatusId(2);
		transferService.sendTransfer(newTransfer);
		viewCurrentBalance();
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				accountService.setAuthToken(currentUser.getToken());
				transferService.setAuthToken(currentUser.getToken());
				//TODO set token for other services
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	private void displayUsers(){
		User[] users = transferService.listAllUsers();
		System.out.println("********************************");
		System.out.println("User:");
		System.out.println("ID:          Username:");
		System.out.println("********************************");

		for (User user : users) {
			System.out.println(user.getId() + "         " + user.getUsername());
		}

		System.out.println("********************************");
	}

	private void displaySentTransfersBanner() {
		System.out.println("***********************************");
		System.out.println("Sent Transfers:");
		System.out.println("ID          To          Amount");
		System.out.println("***********************************");
	}

	private void displayReceivedTransfersBanner() {
		System.out.println("***********************************");
		System.out.println("Received Transfers:");
		System.out.println("ID          From        Amount");
		System.out.println("***********************************");
	}

	private void displayTransferDetailsBanner() {
		System.out.println("***********************************");
		System.out.println("Transfer Details");
		System.out.println("***********************************");
	}

	private String getStatus(int statusId) {
    	String status;
    	if (statusId == 1) {
    		status = "Pending";
		} else if (statusId == 2) {
    		status = "Approved";
		} else {
			status = "Rejected";
		}
    	return status;
	}

	private String getType(int typeId) {
    	String type;
    	if (typeId == 1) {
    		type = "Request";
		} else {
    		type = "Send";
		}
    	return type;
	}

}
