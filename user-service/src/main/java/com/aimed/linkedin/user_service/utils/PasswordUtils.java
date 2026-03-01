package com.aimed.linkedin.user_service.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Hash a password for the first time
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Check that the plain text password matches the previously hashed password
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
