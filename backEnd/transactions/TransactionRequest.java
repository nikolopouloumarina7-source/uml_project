package backEnd.transactions;

import backEnd.user.User;

public class TransactionRequest {

    private final TransactionType type;
    private final String sourceIban;
    private final String targetIban;
    private final double amount;
    private final String description;
 // Προσθήκη του χρήστη που εκτελεί τη συναλλαγή
    private final User transactor;

    public TransactionRequest(TransactionType type,
    						  User transactor, 
                              String sourceIban,
                              String targetIban,
                              double amount,
                              String description) {
        this.type = type;
        this.transactor = transactor;
        this.sourceIban = sourceIban;
        this.targetIban = targetIban;
        this.amount = amount;
        this.description = description;
    }

    public TransactionType getType() { return type; }
    public User getTransactor() { return transactor; }
    public String getSourceIban() { return sourceIban; }
    public String getTargetIban() { return targetIban; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
}
