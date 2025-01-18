package utils;

/**
 * Enum to represent user roles in the library system.
 */
public enum UserRole {
    ADMIN,  // Full access to manage the library system
    LIBRARIAN,      // Access to manage books and patron records
    PATRON;         // Limited access to borrowing and account features
}
