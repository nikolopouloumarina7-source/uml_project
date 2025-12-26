package backEnd.transactions;

import backEnd.bankAccounts.AccountManager;
import backEnd.bankAccounts.BankAccounts;
import backEnd.statements.StatementEntry;
import backEnd.statements.StatementManager;

import java.time.LocalDateTime;

public class InternalTransferCommand implements TransactionCommand {

    private final TransactionRequest request;
    private TransactionResult result;

 // Πεδία για αποθήκευση (Caching) των λογαριασμών από το validate
    private BankAccounts senderAccount;
    private BankAccounts receiverAccount;
    
    public InternalTransferCommand(TransactionRequest request) {
        this.request = request;
    }

    @Override
    public void validate() throws TransactionException {

    	// 1. Έλεγχος ποσού
        if (request.getAmount() <= 0)
            throw new TransactionException("Το ποσό πρέπει να είναι μεγαλύτερο από 0.");

     // 2. Έλεγχος ότι δεν στέλνει στον ίδιο λογαριασμό
        if (request.getSourceIban().equals(request.getTargetIban())) {
            throw new TransactionException("Δεν μπορείτε να κάνετε μεταφορά στον ίδιο λογαριασμό.");
        }
        
//        BankAccounts sender = AccountManager.getAccountByIban(request.getSourceIban());
//        BankAccounts receiver = AccountManager.getAccountByIban(request.getTargetIban());

     // 3. Εύρεση και αποθήκευση Sender
        this.senderAccount = AccountManager.getAccountByIban(request.getSourceIban());
        if (this.senderAccount == null)
            throw new TransactionException("Ο λογαριασμός αποστολέα δεν βρέθηκε.");

     // 4. Εύρεση και αποθήκευση Receiver
    // Εφόσον είναι Ενδοτραπεζική (Internal), ο παραλήπτης ΠΡΕΠΕΙ να βρεθεί στο σύστημά μας
        this.receiverAccount = AccountManager.getAccountByIban(request.getTargetIban());
        if (this.receiverAccount == null)
            throw new TransactionException("Ο λογαριασμός παραλήπτη δεν βρέθηκε.");

     // 5. Έλεγχος Υπολοίπου
        if (this.receiverAccount .getBalance() < request.getAmount())
            throw new TransactionException("Μη επαρκές υπόλοιπο.");
    }

    @Override
    public void execute() throws TransactionException {

    	// Έλεγχος ασφαλείας
        if (this.senderAccount == null || this.receiverAccount == null) {
            throw new TransactionException("Εσωτερικό σφάλμα: Οι λογαριασμοί δεν ορίστηκαν στο validate.");
        }
        
//        BankAccounts sender = AccountManager.getAccountByIban(request.getSourceIban());
//        BankAccounts receiver = AccountManager.getAccountByIban(request.getTargetIban());

        BankAccounts sender = this.senderAccount;
        BankAccounts receiver = this.receiverAccount;
        
        double amount = request.getAmount();

     // 1. Ενημέρωση Υπολοίπων
        double senderAfter = sender.getBalance() - amount;
        double receiverAfter = receiver.getBalance() + amount;

        sender.setBalance(senderAfter);
        receiver.setBalance(receiverAfter);

     // 2. Αποθήκευση αλλαγών
        AccountManager.updateAccount(sender);
        AccountManager.updateAccount(receiver);

     // 3. Δημιουργία Statements (Διπλή εγγραφή)
        	// Statement Αποστολέα
        StatementManager.getInstance().addEntry(
                sender.getIBAN(),
                new StatementEntry.Builder() 
                .setDateTime(LocalDateTime.now())
                .setType("INTERNAL_TRANSFER_SENT")
                .setAmount(-amount)
                .setBalanceAfter(senderAfter)
                .setDescription("Προς: " + receiver.getIBAN() + " - " + request.getDescription())
                .build()
        );

        		// Statement Παραλήπτη
        StatementManager.getInstance().addEntry(
                receiver.getIBAN(),
                new StatementEntry.Builder() 
                .setDateTime(LocalDateTime.now())
                .setType("INTERNAL_TRANSFER_RECEIVED")
                .setAmount(amount)
                .setBalanceAfter(receiverAfter)
                .setDescription("Από: " + sender.getIBAN() + " - " + request.getDescription())
                .build()
        );

     // 4. Αποτέλεσμα
        result = new TransactionResult.Builder()
                .success(true)
                .message("Η μεταφορά ολοκληρώθηκε με επιτυχία!")
                .sourceBalanceAfter(senderAfter)
                .targetBalanceAfter(receiverAfter)
                .build();
    }

    @Override
    public TransactionResult getResult() {
        return result;
    }
}
