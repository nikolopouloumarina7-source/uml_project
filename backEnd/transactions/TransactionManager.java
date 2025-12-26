package backEnd.transactions;

public class TransactionManager implements TransactionService {

    private static TransactionManager instance;

    // Private constructor για Singleton
    private TransactionManager() {}

    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    /**
     * Υλοποίηση της μεθόδου του Interface.
     * Δέχεται Request -> Φτιάχνει Command -> Εκτελεί -> Καταγράφει -> Επιστρέφει.
     */
    @Override
    public TransactionResult execute(TransactionRequest request) {
        TransactionResult result;
        
        try {
            // 1. Δημιουργία Command από το Factory
            TransactionCommand command = TransactionFactory.createCommand(request);

            // 2. Επικύρωση (Validation)
            command.validate();

            // 3. Εκτέλεση (Execution - Business Logic + Statements)
            command.execute();

            // 4. Λήψη επιτυχούς αποτελέσματος
            result = command.getResult();

        } catch (TransactionException e) {
            // 5. Διαχείριση γνωστών λαθών (π.χ. ανεπαρκές υπόλοιπο)
            result = new TransactionResult.Builder()
                    .success(false)
                    .message(e.getMessage())
                    .sourceBalanceAfter(0) // Default τιμές
                    .targetBalanceAfter(0)
                    .build();

        } catch (Exception e) {
            // 6. Διαχείριση απρόβλεπτων λαθών
            e.printStackTrace();
            result = new TransactionResult.Builder()
                    .success(false)
                    .message("Συστημικό Σφάλμα: " + e.getMessage())
                    .build();
        }

        // 7. Καταγραφή στο Audit Log (Απαίτηση FR-25, NFR-03)
        TransactionLogger.log(request, result);

        return result;
    }
}