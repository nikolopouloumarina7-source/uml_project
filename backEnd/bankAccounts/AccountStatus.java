package backEnd.bankAccounts;

/**
 * Represents the lifecycle status of a bank account.
 *
 * PENDING  : Account has been requested but not yet approved by Admin
 * ACTIVE   : Account is approved and can perform transactions
 * INACTIVE : Account is disabled and cannot perform transactions
 *
 * Used to satisfy:
 * FR-06, FR-07, FR-10 (Phase 2 SRS)
 */
public enum AccountStatus {
    PENDING,
    ACTIVE,
    INACTIVE
}
