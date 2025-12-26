package backEnd.transactions;

/**
 * Το TransactionService ορίζει το συμβόλαιο για το κεντρικό σημείο
 * εκτέλεσης όλων των συναλλαγών στο σύστημα (TransactionManager).
 */
public interface TransactionService {

    /**
     * Επεξεργάζεται ένα αίτημα συναλλαγής (TransactionRequest).
     * Αυτή η μέθοδος είναι υπεύθυνη για:
     * 1. Τη δημιουργία του κατάλληλου Command (μέσω του Factory).
     * 2. Την επικύρωση (validate) της συναλλαγής.
     * 3. Την εκτέλεση (execute) της συναλλαγής.
     * 4. Την καταγραφή στο Audit Log.
     * 5. Την επιστροφή του αποτελέσματος.
     *
     * @param request Το αντικείμενο που περιέχει τα δεδομένα της συναλλαγής (π.χ. IBANs, ποσό, τύπος).
     * @return Το αποτέλεσμα της συναλλαγής (επιτυχία/αποτυχία, μήνυμα, νέα υπόλοιπα).
     */
    TransactionResult execute(TransactionRequest request);
}