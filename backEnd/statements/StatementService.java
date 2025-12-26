package backEnd.statements;

public interface StatementService {
    void addEntry(String iban, StatementEntry entry);
}
