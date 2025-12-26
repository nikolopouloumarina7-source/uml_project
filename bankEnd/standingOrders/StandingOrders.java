package bankEnd.standingOrders;

import java.time.LocalDate;
import java.util.ArrayList;

import bankAccounts.BankAccount;
import user.Customer;

import command.Command;

public abstract class StandingOrders implements Command {

	  private Double amount;
	  private LocalDate startDate;
	  private LocalDate endDate;
	  private LocalDate nextExecutionDate;
	  
	  private String orderId;
	  private String title;
	  private String description;
	  
	  private BankAccounts chargeAccount;
	  private Customer customer;
	  
	  private Double fee;

	  
	  private int numOfTries;
	  private int numOfFails;
	  private boolean active;
	  private ArrayList<LocalDate> datesThatFailed;
	 
	  

 
	  

	  public StandingOrders(LocalDate startDate, LocalDate endDate, LocalDate nextExecutionDate, String orderId,
		        String title, String description, BankAccount chargeAccount, Customer customer, Double fee, Double amount,
		        int numOfTries, int numOfFails, boolean active, ArrayList<LocalDate> datesThatFailed) {
		    this.startDate = startDate;
		    this.endDate = endDate;
		    this.nextExecutionDate = nextExecutionDate;
		    this.orderId = orderId;
		    this.title = title;
		    this.description = description;
		    this.chargeAccount = chargeAccount;
		    this.customer = customer;
		    this.fee = fee;
		    this.numOfTries = numOfTries;
		    this.numOfFails = numOfFails;
		    this.active = active;
		    this.datesThatFailed = datesThatFailed;
		}

	// Getters and Setters
	protected LocalDate getStartDate() {
		return startDate;
	}

	protected void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	protected LocalDate getEndDate() {
		return endDate;
	}

	private void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	protected LocalDate getNextExecutionDate() {
		return nextExecutionDate;
	}

	protected void setNextExecutionDate(LocalDate nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}

	private String getOrderId() {
		return orderId;
	}

	protected void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	protected String getTitle() {
		return title;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	protected BankAccounts getChargeAccount() {
		return chargeAccount;
	}

	protected void setChargeAccount(BankAccounts chargeAccount) {
		this.chargeAccount = chargeAccount;
	}

	protected Customer getCustomer() {
		return customer;
	}

	protected void setCustomer(Customer customer) {
		this.customer = customer;
	}

	protected Double getFee() {
		return fee;
	}

	protected void setFee(Double fee) {
		this.fee = fee;
	}

	protected Double getAmount() {
		return amount;
	}

	protected void setAmount(Double amount) {
		this.amount = amount;
	}

	protected int getNumOfTries() {
		return numOfTries;
	}

	protected void setNumOfTries(int numOfTries) {
		this.numOfTries = numOfTries;
	}

	protected int getNumOfFails() {
		return numOfFails;
	}

	protected void setNumOfFails(int numOfFails) {
		this.numOfFails = numOfFails;
	}

	protected boolean isActive() {
		return active;
	}

	protected void setActive(boolean active) {
		this.active = active;
	}

	protected ArrayList<LocalDate> getDatesThatFailed() {
		return datesThatFailed;
	}

	protected void setDatesThatFailed(ArrayList<LocalDate> datesThatFailed) {
		this.datesThatFailed = datesThatFailed;
	}

	public void execute() {	
	} 
	
	protected abstract void reschedule();
	
	  /** Έλεγχος αν πρέπει να εκτελεστεί σήμερα */
    public boolean isDue(LocalDate today) {
        return active
                && !today.isBefore(startDate)
                && (endDate == null || !today.isAfter(endDate))
                && today.equals(nextExecutionDate);
    }

    protected void markSuccess() {
        numOfTries++;
    }

    protected void markFailure(LocalDate date) {
        numOfFails++;
        datesThatFailed.add(date);
    }

    protected void deactivate() {
        active = false;
    }
    
}


	
