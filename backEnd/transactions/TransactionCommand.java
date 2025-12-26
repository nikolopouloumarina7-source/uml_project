package backEnd.transactions;

public interface TransactionCommand {
	void validate() throws TransactionException;
    void execute() throws TransactionException;
    TransactionResult getResult();
}
