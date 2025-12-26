package backEnd.transactions;

import backEnd.bankAccounts.AccountManager;
import backEnd.bankAccounts.BankAccounts;
import backEnd.statements.StatementEntry;
import backEnd.statements.StatementManager;

import java.time.LocalDateTime;

public class InterbankTransferCommand implements TransactionCommand {

    private static final double FEE = 0.50;

    private final TransactionRequest request;
    private TransactionResult result;
    
    private BankAccounts senderAccount;

    public InterbankTransferCommand(TransactionRequest request) {
        this.request = request;
    }

    @Override
    public void validate() throws TransactionException {

        if (request.getAmount() <= 0)
            throw new TransactionException("Το ποσό πρέπει να είναι μεγαλύτερο από 0.");

//        BankAccounts sender = AccountManager.getAccountByIban(request.getSourceIban());

        this.senderAccount = AccountManager.getAccountByIban(request.getSourceIban());  
        if (this.senderAccount == null)
            throw new TransactionException("Ο λογαριασμός αποστολέα δεν βρέθηκε.");

        double total = request.getAmount() + FEE;

        if (this.senderAccount.getBalance() < total)
            throw new TransactionException("Μη επαρκές υπόλοιπο για το ποσό + προμήθεια 0.50€.");
    }

    @Override
    public void execute() throws TransactionException {

    	// Έλεγχος ασφαλείας
        if (this.senderAccount == null) {
            throw new TransactionException("Εσωτερικό σφάλμα: Ο λογαριασμός δεν ορίστηκε στο validate.");
        }
        
//        BankAccounts sender = AccountManager.getAccountByIban(request.getSourceIban());

     // Χρήση του cached λογαριασμού
        BankAccounts sender = this.senderAccount;
        
        double amount = request.getAmount();
        double total = amount + FEE;

        double senderAfter = sender.getBalance() - total;

        sender.setBalance(senderAfter);
        AccountManager.updateAccount(sender);

     // Statement
        StatementManager.getInstance().addEntry(
                sender.getIBAN(),
                new StatementEntry.Builder()
                .setDateTime(java.time.LocalDateTime.now())
                .setType("INTERBANK_TRANSFER_SENT")
                .setAmount(-total) // Το συνολικό ποσό χρέωσης (αρνητικό)
                .setBalanceAfter(senderAfter)
                .setDescription(request.getDescription() + " (Ποσό: " + amount + "€, Προμήθεια: " + FEE + "€)")
                .build()
        );

        result = new TransactionResult.Builder()
                .success(true)
                .message("Η διατραπεζική μεταφορά ολοκληρώθηκε με επιτυχία!")
                .sourceBalanceAfter(senderAfter)
                .build();
    }

    @Override
    public TransactionResult getResult() {
        return result;
    }
}
