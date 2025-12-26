package backEnd.transactions;

import backEnd.bankAccounts.AccountManager;
import backEnd.bankAccounts.BankAccounts;
import backEnd.statements.StatementEntry;
import backEnd.statements.StatementManager;

import java.time.LocalDateTime;


public class WithdrawalCommand implements TransactionCommand {

    private TransactionRequest request;
    private TransactionResult result;
    
    private BankAccounts sourceAccount;

    public WithdrawalCommand(TransactionRequest request) {
        this.request = request;
    }

    
    
    @Override
    public void validate() throws TransactionException { 
    	if (request.getAmount() <= 0) {
            throw new TransactionException("Το ποσό πρέπει να είναι μεγαλύτερο από 0.");
        }

    	this.sourceAccount = AccountManager.getAccountByIban(request.getSourceIban());
        if (this.sourceAccount == null) {
            throw new TransactionException("Ο λογαριασμός δεν βρέθηκε.");
        }

        if (this.sourceAccount.getBalance() < request.getAmount()) {
            throw new TransactionException("Μη επαρκές υπόλοιπο για ανάληψη.");
        }
    }

    
    
    
    @Override
    public void execute() throws TransactionException { 
    	
    	// Έλεγχος ασφαλείας
        if (this.sourceAccount == null) {
            throw new TransactionException("Εσωτερικό σφάλμα: Ο λογαριασμός δεν ορίστηκε στο validate.");
        }
    	
 //   	BankAccounts acc = AccountManager.getAccountByIban(request.getSourceIban());

        BankAccounts acc = this.sourceAccount;
        
        double before = acc.getBalance();
        double after = before - request.getAmount();

        acc.setBalance(after);
        AccountManager.updateAccount(acc);

        // Statement
        StatementManager.getInstance().addEntry(
                acc.getIBAN(),
                new StatementEntry.Builder() 
                .setDateTime(LocalDateTime.now())
                .setType("WITHDRAWAL")
                .setAmount(-request.getAmount()) // Αρνητικό ποσό για χρέωση
                .setBalanceAfter(after)
                .setDescription(request.getDescription())
                .build()
        );

        // Result for UI
        result = new TransactionResult.Builder()
                .success(true)
                .message("Η ανάληψη ολοκληρώθηκε με επιτυχία!")
                .sourceBalanceAfter(after)
                .build();
    }

    
    
    
    @Override
    public TransactionResult getResult() { 
    	return result; 
    }
}
