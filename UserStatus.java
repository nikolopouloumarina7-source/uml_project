package backEnd.user;

/**
 * Represents the authentication status of a user.
 *
 * ACTIVE   : User can login normally
 * LOCKED  : User is locked after 3 failed login attempts
 */
public enum UserStatus {
    ACTIVE,
    LOCKED
}
