package backEnd.transactions;

import backEnd.bankAccounts.AccountManager;
import backEnd.bankAccounts.BankAccounts;
import backEnd.statements.StatementEntry;
import backEnd.statements.StatementManager; 

import java.time.LocalDateTime;

public class DepositCommand implements TransactionCommand {

    private TransactionRequest request;
    private TransactionResult result;
 // Το πεδίο που θα κρατήσει τον λογαριασμό μεταξύ validate και execute
    private BankAccounts targetAccount;

    public DepositCommand(TransactionRequest request) {
        this.request = request;
    }

    
    
    
    @Override
    public void validate() throws TransactionException {
        // TODO 
    	
    	 if (request.getAmount() <= 0) {
             throw new TransactionException("Το ποσό πρέπει να είναι μεγαλύτερο από 0.");
         }

    	 this.targetAccount = AccountManager.getAccountByIban(request.getTargetIban());
    	 if (this.targetAccount == null) {
             throw new TransactionException("Ο λογαριασμός δεν βρέθηκε.");
         }
    
    } 

    
   
    
    @Override
    public void execute() throws TransactionException {
        // TODO 
    	
    	// Ελέγχουμε αν ο λογαριασμός βρέθηκε στο validate.
        if (targetAccount == null) {
            throw new TransactionException("Εσωτερικό σφάλμα. Ο λογαριασμός-στόχος δεν έχει οριστεί.");
        }
        
//        BankAccounts acc = AccountManager.getAccountByIban(request.getTargetIban());

     // Χρησιμοποιούμε το targetAccount που βρήκαμε στο validate
        BankAccounts acc = this.targetAccount;
        
     // 1. Υπολογισμός και Ενημέρωση υπολοίπου
        double before = acc.getBalance();
        double after = before + request.getAmount();

        acc.setBalance(after);
        AccountManager.updateAccount(acc);

     // 2. Καταχώρηση Statement (Χρησιμοποιώντας το StatementServiceImpl)
        StatementManager.getInstance().addEntry(
                acc.getIBAN(),
                new StatementEntry.Builder()  
                .setDateTime(LocalDateTime.now())
                .setType("DEPOSIT")
                .setAmount(request.getAmount())
                .setBalanceAfter(after)
                .setDescription(request.getDescription())
                .build()
        );

     // 3. Δημιουργία Αποτελέσματος (Builder Pattern)
        // return to UI
        result = new TransactionResult.Builder()
                .success(true)
                .message("Η κατάθεση ολοκληρώθηκε με επιτυχία!")
                .sourceBalanceAfter(after)
                .build();
    }

    
    
    
    
    
    @Override
    public TransactionResult getResult() {
        return result;
    }
}
