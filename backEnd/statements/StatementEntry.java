package backEnd.statements;

import java.time.LocalDateTime;

public class StatementEntry {

    private final LocalDateTime dateTime;
    private final String type;          // π.χ. DEPOSIT, WITHDRAWAL, TRANSFER_SENT
    private final double amount;        // Θετικό για πίστωση, Αρνητικό για χρέωση
    private final double balanceAfter;  // Το υπόλοιπο μετά τη συναλλαγή
    private final String description;   // Αιτιολογία ή RF κωδικός

    // Private Constructor: Μόνο ο Builder μπορεί να φτιάξει αντικείμενα
    private StatementEntry(Builder builder) {
        this.dateTime = builder.dateTime;
        this.type = builder.type;
        this.amount = builder.amount;
        this.balanceAfter = builder.balanceAfter;
        this.description = builder.description;
    }

    // Getters
    public LocalDateTime getDateTime() { return dateTime; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "StatementEntry{" +
                "dateTime=" + dateTime +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", description='" + description + '\'' +
                '}';
    }

    // --- BUILDER PATTERN ---
    public static class Builder {
        private LocalDateTime dateTime;
        private String type;
        private double amount;
        private double balanceAfter;
        private String description;

        public Builder setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setBalanceAfter(double balanceAfter) {
            this.balanceAfter = balanceAfter;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public StatementEntry build() {
            // Εδώ θα μπορούσατε να βάλετε ελέγχους εγκυρότητας αν χρειαζόταν
            if (this.dateTime == null) {
                this.dateTime = LocalDateTime.now(); // Default τιμή
            }
            return new StatementEntry(this);
        }
    }
}