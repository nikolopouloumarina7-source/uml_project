package backEnd.statements;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class StatementManager implements StatementService {

	// 1. Static Instance για το Singleton
    private static StatementManager instance;
    
    // 2. Private Constructor (για να μην φτιάχνεται αλλού)
    private StatementManager() {
        // statements HashMap initialized here
    }
    
    // 3. Public Static Getter
    public static StatementManager getInstance() {
        if (instance == null) {
            instance = new StatementManager();
        }
        return instance;
    }
    
    private HashMap<String, List<StatementEntry>> statements = new HashMap<>();

    @Override
    public void addEntry(String iban, StatementEntry entry) {
        statements.putIfAbsent(iban, new ArrayList<>());
        statements.get(iban).add(entry);
    }
    
 // μέθοδο για ανάγνωση (για το GUI αργότερα)
    public List<StatementEntry> getStatementsForIban(String iban) {
        return statements.getOrDefault(iban, new ArrayList<>());
    }
}
