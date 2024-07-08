package org.example.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and checking passwords using BCrypt.
 */
public class PasswordUtil {

    /**
     * Generates a BCrypt hash of the given plain password.
     *
     * @param plainPassword the plain password to hash
     * @return the BCrypt hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Checks if the given plain password matches the BCrypt hashed password.
     *
     * @param plainPassword  the plain password to check
     * @param hashedPassword the BCrypt hashed password to compare against
     * @return true if the plain password matches the hashed password, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
