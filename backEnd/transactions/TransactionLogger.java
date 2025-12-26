package backEnd.transactions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import backEnd.user.User;

public class TransactionLogger {

    private static final String LOG_FILE = "data/audit_log.txt";

    // Static μέθοδος για να καλείται εύκολα από τον TransactionManager
    public static void log(TransactionRequest request, TransactionResult result) {
        
        // Μορφή του Log: TIMESTAMP | TYPE | USER | SUCCESS | MESSAGE
        String logEntry = String.format("%s | %s | %s | %s | %s",
                LocalDateTime.now(),
                request.getType(),
                (request.getTransactor() != null ? request.getTransactor().getUsername() : "Unknown"), // Υποθέτουμε ότι ο User έχει getUsername()
                (result.isSuccess() ? "SUCCESS" : "FAIL"),
                result.getMessage()
        );

        // 1. Εκτύπωση στην κονσόλα (για debug/demo)
        System.out.println("[AUDIT] " + logEntry);

        // 2. (Προαιρετικά) Εγγραφή σε αρχείο για να ικανοποιηθεί το Audit Policy
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(logEntry);
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την εγγραφή στο Audit Log: " + e.getMessage());
        }
    }
}