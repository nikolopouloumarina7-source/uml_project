package backEnd.transactions;

import backEnd.bankAccounts.AccountManager;
import backEnd.bankAccounts.BankAccounts;
import backEnd.bills.Bill;
import backEnd.bills.BillService;
import backEnd.statements.StatementEntry;
import backEnd.statements.StatementManager;

import java.time.LocalDateTime;

public class RfPaymentCommand implements TransactionCommand {

    private final TransactionRequest request;
    private TransactionResult result;
    
 // Πεδία για αποθήκευση (Caching)
    private Bill bill;
    private BankAccounts customerAccount;
    private BankAccounts businessAccount;

    public RfPaymentCommand(TransactionRequest request) {
        this.request = request;
    }

    @Override
    public void validate() throws TransactionException {

     // 1. Ανάκτηση RF Code (Χρησιμοποιούμε το targetIban ως RF Code)
        String rfCode = request.getTargetIban();
        
     // 2. Εύρεση Bill
        this.bill = BillService.getInstance().getBillByRf(rfCode);
        if (this.bill == null)
            throw new TransactionException("Ο λογαριασμός με κωδικό RF " + rfCode + " δεν βρέθηκε.");

     // 3. Έλεγχος αν είναι ήδη πληρωμένο
        if (!this.bill.isPending())
            throw new TransactionException("Το bill έχει ήδη πληρωθεί.");

     // 4. Εύρεση Πελάτη (Πληρωτής)
        this.customerAccount = AccountManager.getAccountByIban(request.getSourceIban());
        if (this.customerAccount == null) {
            throw new TransactionException("Ο λογαριασμός χρέωσης δεν βρέθηκε.");
        }
        
     // 5. Εύρεση Επιχείρησης (Δικαιούχος - από το Bill)
//      BankAccounts customer = AccountManager.getAccountByIban(request.getSourceIban());
        this.businessAccount = AccountManager.getAccountByIban(this.bill.getBusinessIban());
        if (this.businessAccount == null) {
            throw new TransactionException("Ο λογαριασμός της επιχείρησης (δικαιούχου) δεν βρέθηκε.");
        }
        
     // 6. Έλεγχος Υπολοίπου
        if (this.customerAccount.getBalance() < bill.getAmount())
            throw new TransactionException("Μη επαρκές υπόλοιπο.");
    }

    @Override
    public void execute() throws TransactionException {

    	// Έλεγχος ασφαλείας
        if (this.bill == null || this.customerAccount == null || this.businessAccount == null) {
            throw new TransactionException("Εσωτερικό σφάλμα: Τα δεδομένα δεν ορίστηκαν στο validate.");
        }
        
//        Bill bill = BillService.getInstance().getBillByRf(request.getRfCode());
//        BankAccounts customer = AccountManager.getAccountByIban(request.getSourceIban());
//        BankAccounts business = AccountManager.getAccountByIban(bill.getBusinessIban());

        double amount = bill.getAmount();

     //1. ενημέρωση υπολοίπων
        double newCustomerBalance = this.customerAccount.getBalance() - amount;
        double newBusinessBalance = this.businessAccount.getBalance() + amount;

        this.customerAccount.setBalance(newCustomerBalance);
        this.businessAccount.setBalance(newBusinessBalance);

     // 2. Αποθήκευση αλλαγών στους λογαριασμούς
        AccountManager.updateAccount(this.customerAccount);
        AccountManager.updateAccount(this.businessAccount);

     // 3. Ενημέρωση κατάστασης Bill 
        BillService.getInstance().markAsPaid(this.bill);
        
     // 4. Δημιουργία Statements
        			// Χρέωση Πελάτη
        StatementManager.getInstance().addEntry(
                this.customerAccount.getIBAN(),
                new StatementEntry.Builder()
                .setDateTime(LocalDateTime.now())
                .setType("RF_PAYMENT_DEBIT")
                .setAmount(-amount)
                .setBalanceAfter(newCustomerBalance)
                .setDescription("RF Payment " + this.bill.getRfCode() + " (" + this.bill.getIssuer().getLegalName() + ")")
                .build()
        );

        			// Πίστωση Επιχείρησης
        StatementManager.getInstance().addEntry(
                this.businessAccount.getIBAN(),
                new StatementEntry.Builder() 
                .setDateTime(LocalDateTime.now())
                .setType("RF_PAYMENT_CREDIT")
                .setAmount(amount)
                .setBalanceAfter(newBusinessBalance)
                .setDescription("Είσπραξη από RF: " + this.bill.getRfCode())
                .build()
        );

     // 5. Result
        result = new TransactionResult.Builder()
                .success(true)
                .message("Η πληρωμή ολοκληρώθηκε με επιτυχία!")
                .sourceBalanceAfter(newCustomerBalance)
                .targetBalanceAfter(newBusinessBalance)
                .build();
    }

    @Override
    public TransactionResult getResult() {
        return result;
    }
}
