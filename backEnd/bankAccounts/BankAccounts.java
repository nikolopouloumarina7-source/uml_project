package backEnd.bankAccounts;

import backEnd.user.Customer;
import backEnd.store.Storable;

public abstract class BankAccounts implements Storable {

    private String IBAN;
    private float rate;
    private double balance;
    private Customer primaryOwner;
    private String dateCreated;
    private double interest;
    private AccountStatus status;

    protected BankAccounts(String iBAN, float rate, double balance,
                           Customer primaryOwner, String dateCreated) {
        this.IBAN = iBAN;
        this.rate = rate;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.dateCreated = dateCreated;
        this.interest = 0;
        this.status = AccountStatus.PENDING; // FR-06
    }

    // ---------------- STATUS ----------------
    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    protected void ensureActive() {
        if (!isActive()) {
            throw new IllegalStateException("Account is not ACTIVE");
        }
    }

    // ---------------- GETTERS ----------------
    public String getIBAN() { return IBAN; }
    public float getRate() { return rate; }
    public double getBalance() { return balance; }
    protected Customer getPrimaryOwner() { return primaryOwner; }
    protected String getDateCreated() { return dateCreated; }
    protected double getInterest() { return interest; }

    // ---------------- SETTERS ----------------
    protected void setIBAN(String iBAN) { IBAN = iBAN; }
    protected void setRate(float rate) { this.rate = rate; }
    protected void setPrimaryOwner(Customer primaryOwner) { this.primaryOwner = primaryOwner; }
    protected void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
    protected void setInterest(double interest) { this.interest = interest; }

    public void setBalance(double balance) {
        ensureActive(); // ❗ BLOCK INACTIVE
        this.balance = balance;
    }

    public abstract String getAccountType();

    // ---------------- STORAGE ----------------
    @Override
    public String marshal() {
        return "type:" + getClass().getSimpleName() +
               ",iban:" + IBAN +
               ",primaryOwner:" + primaryOwner.getVat() +
               ",dateCreated:" + dateCreated +
               ",rate:" + rate +
               ",balance:" + balance +
               ",status:" + status;
    }
}
